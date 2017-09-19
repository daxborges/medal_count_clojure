(ns medal-count-clj.views
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [clojure.string :refer [lower-case]]))

(defn sort-header
  "Render the sorting header"
  []
  (let [srt-by @(subscribe [:sort-by])
        all-classes "mc--border-grey mc--border-thick mc--border-bottom"
        selected-classes "mc--border-grey mc--border-thick mc--border-top mc--th-selected"
        circle-button-classes "mc--clean-button mc--circle-button mc--circle mc--vert-align-middle"
        render-select-class (fn [sort srt-by]
                              (if (= sort srt-by)
                                selected-classes
                                ""))]
    [:thead
     [:tr
     [:th {:class all-classes}]
     [:th {:class all-classes}]
     [:th {:class all-classes}]
     [:th {:class all-classes}]

     [:th {:class (str all-classes " " (render-select-class :gold srt-by))}
      [:button {:class (str "mc--bg-gold" " " circle-button-classes)
                :title "gold"
                :on-click #(dispatch [:set-sort :gold])}]]

     [:th {:class (str all-classes " " (render-select-class :silver srt-by))}
      [:button {:class (str "mc--bg-silver" " " circle-button-classes)
                :title "silver"
                :on-click #(dispatch [:set-sort :silver])}]]

     [:th {:class (str all-classes " " (render-select-class :bronze srt-by))}
      [:button {:class (str "mc--bg-bronze" " " circle-button-classes)
                :title "bronze"
                :on-click #(dispatch [:set-sort :bronze])}]]

     [:th {:class (str all-classes " " (render-select-class :total srt-by))}
      [:button {:class "mc--clean-button mc--bg-none mc--text-upper mc--text-dark-grey mc--text-size-1-1 mc--text-space-neg-0-5 mc--line-height-20 mc--vert-align-middle"
                :on-click #(dispatch [:set-sort :total])}
       "TOTAL"]]
     ]]))

(defn country-row
  "Render a country row"
  []
  (fn [{:keys [code place gold silver bronze total]}]
    (let [thin-bottom-class "mc--clean-table-item mc--border-light-grey mc--border-thin mc--border-bottom"
          flag-class #(str "mc--flag-" (lower-case %))]
      [:tr
       [:td {:class thin-bottom-class} place]
       [:td {:class thin-bottom-class}
        [:span {:class (str "mc--flag" " " (flag-class code))
                :title (str "flag " code)} ""]]
       [:td {:class (str thin-bottom-class " " "mc--text-bold")} code]
       [:td {:class thin-bottom-class} ""]
       [:td {:class thin-bottom-class} gold]
       [:td {:class thin-bottom-class} silver]
       [:td {:class thin-bottom-class} bronze]
       [:td {:class (str thin-bottom-class " " "mc--text-bold mc--text-dark-grey")} total]
       ])))

(defn top-ten-countries
  "Render the top ten countries"
  []
  (let [sorted-countries (take 10 @(subscribe [:sorted-countries]))]
    [:tbody (for [country  sorted-countries]
              ^{:key (:code country)} [country-row country])]
    ))


(defn medal-count-clj-app
  "Entry Point - Render the app"
  []
  [:div
   {:class "mc--sans-font mc--text-grey mc--padding-5"}
   [:h2
    {:class "mc--margin-top-5 mc--margin-bottom-10 mc--text-upper mc--text-lighter mc--text-space-neg-0-5"}
    "Medal Count"]
   [:table
    {:class "mc--text-center mc--clean-table-item mc--table"}
    [sort-header]
    [top-ten-countries]
    ]])