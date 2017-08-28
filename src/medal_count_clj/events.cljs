(ns medal-count-clj.events
  (:require
    [medal-count-clj.db :refer [default-db countries->local-store]]
    [re-frame.core :refer [reg-event-db reg-event-fx inject-cofx path trim-v
                           after debug]]
    [day8.re-frame.http-fx]
    [ajax.core :as ajax]
    [cljs.spec     :as s]))

;; -- Interceptors --------------------------------------------------------------
(defn check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

;; Checks the whole medal-count db
(def check-spec-interceptor (after (partial check-and-throw :medal-count-clj.db/db)))


(def ->local-store (after countries->local-store))


(def countries-interceptors [check-spec-interceptor               ;; ensure the spec is still valid  (after)
                        (path :countries)                        ;; 1st param to handler will be the value from this path within db
                        ->local-store                        ;; write todos to localstore  (after)
                        (when ^boolean js/goog.DEBUG debug)  ;; look at the js browser console for debug logs
                        trim-v])


;; xhrio info for getting the countries JSON
(def countries-xhrio {:method          :get
                      :uri             "//s3-us-west-2.amazonaws.com/reuters.medals-widget/medals.json"
                      :response-format (ajax/json-response-format {:keywords? true})  ;; IMPORTANT!: You must provide this.
                      :on-success      [:countries-http-success]
                      :on-failure      [:countries-http-fail]})


(defn add-total-to-country
  "adds total value to a country"
  [{:keys [gold bronze silver] :as country}]
  (assoc country :total (+ gold bronze silver)))


;; Handle success messages
(reg-event-db
  :countries-http-success

  [countries-interceptors]

  (fn [_ [result]]
    (let [countries (reduce (fn [smap {:keys [code] :as country}]
                              (assoc smap code (add-total-to-country country))) (sorted-map)
                            (js->clj result :keywordize-keys true))]
      countries)
    ))

(reg-event-db
  :change-sort

  [check-spec-interceptor (path :sort-by)]

  (fn [_ [_ new-sort]]
    new-sort))


;; Init the DB
(reg-event-fx
  :initialise-db

  [(inject-cofx :local-store-countries)
   check-spec-interceptor]

  (fn [{:keys [db local-store-countries]} _]
    (if (empty? local-store-countries)
      { :db default-db :http-xhrio countries-xhrio} ; get via http
      { :db (assoc default-db :countries local-store-countries)} ; use whats in local storage
      )))