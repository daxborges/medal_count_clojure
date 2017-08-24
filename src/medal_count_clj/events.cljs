(ns medal-count-clj.events
  (:require
    [medal-count-clj.db :refer [default-db]]
    [re-frame.core :refer [reg-event-db reg-event-fx inject-cofx path trim-v
                           after debug]]
    [cljs.spec     :as s]))

;; -- Interceptors --------------------------------------------------------------
(defn check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor (after (partial check-and-throw :medal-count-clj.db/db)))


;; -- Event Handlers ----------------------------------------------------------
(reg-event-db      ;; part of the re-frame API
  :initialise-db     ;; event-id
  [check-spec-interceptor]
  (fn [_ _]     ;; new-sort is one of :total, :gold, :silver or :bronze
    default-db))