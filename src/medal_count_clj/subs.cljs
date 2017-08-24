(ns medal-count-clj.subs
      (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
  :sort-by          ;; usage:   (subscribe [:showing])
  (fn [db _]        ;; db is the (map) value stored in the app-db atom
      (:sort-by db)))