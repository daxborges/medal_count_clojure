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
(s/def ::sort-by
  #{:total
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
   :sort-by :total})