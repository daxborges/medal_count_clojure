(ns medal-count-clj.db
  (:require [cljs.reader]
            [cljs.spec :as s]
            [re-frame.core :as re-frame]))


;; -- Spec --------------------------------------------------------------------

;; Countries
(s/def ::gold int?)
(s/def ::silver int?)
(s/def ::bronze int?)
(s/def ::code string?)
(s/def ::country (s/keys :req-un [::code ::gold ::silver ::bronze]))
(s/def ::countries (s/and
                     (s/map-of ::code ::country)
                     #(instance? PersistentTreeMap %)
                     ))

;; Sort By
(s/def ::sort-by #{:total
                   :gold
                   :silver
                   :bronze
                   })

;; DB Main
(s/def ::db (s/keys :req-un [::countries ::sort-by]))

;; -- Default app-db Value  ---------------------------------------------------
;;
(def default-db
  {:countries (sorted-map)
   :sort-by :gold})


;; -- Local Storage  ----------------------------------------------------------
(def countries-ls-key "countries-medal-count")                         ;; localstore key

(defn countries->local-store
  "Puts countries into localStorage"
  [countries]
  (.setItem js/localStorage countries-ls-key (str countries)))     ;; sorted-map writen as an EDN map


;; -- cofx Registrations  -----------------------------------------------------
(re-frame/reg-cofx
  :local-store-countries
  (fn [cofx _]
    ;; put the localstore todos into the coeffect, under key :local-store-todos
    (assoc cofx :local-store-countries   ;; read in todos from localstore, and process into a sorted map
                (into (sorted-map)
                      (some->> (.getItem js/localStorage countries-ls-key)
                               (cljs.reader/read-string))))))       ;; stored as an EDN map.