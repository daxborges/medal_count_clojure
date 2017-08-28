(ns medal-count-clj.subs
      (:require [re-frame.core :refer [reg-sub subscribe]]))

;; -------------------------------------------------------------------------------------
;; Layer 2

(reg-sub
  :sort-by
  (fn [db _]
      (:sort-by db)))


(reg-sub
  :raw-countries
  (fn [db _]
    (:countries db)))


;; maps the secondary medal ranking
(def sort-secondary { :gold :silver
                     :silver :gold
                     :bronze :gold
                     :total :gold })

(defn compare-medal-values
  "compares the medals for the countries"
  [medal-type & countries]
  (let [medal-values (map medal-type countries)]
    (apply compare medal-values)))


(defn compare-two-countries
  "compare two countries using medal priorities"
  [srt-by c1 c2]
  (let [primary (compare-medal-values srt-by c1 c2)
        secondary (compare-medal-values (get sort-secondary srt-by) c1 c2)]
    (if (= 0 primary)
      secondary
      primary)))

;; -------------------------------------------------------------------------------------
;; Layer 3
(reg-sub
  :sorted-countries

  (fn [query-v _]
    [(subscribe [:raw-countries])
     (subscribe [:sort-by])])

  (fn [[countries srt-by] _]   ;; that 1st parameter is a 2-vector of values
    (let [place-counts (iterate inc 1)
          sorted-countries (sort #(compare-two-countries srt-by (val %2) (val %1)) countries)]
      (map #(assoc (val %1) :place %2) sorted-countries place-counts))))