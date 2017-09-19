(ns medal-count-clj.core
  (:require [bidi.bidi :as bidi]
            [devtools.core :as devtools]
            [goog.events :as events]
            [reagent.core :as reagent]
            [re-frame.core :refer [dispatch dispatch-sync]]
            [medal-count-clj.routes :refer [app-routes]]
            [medal-count-clj.events] ;; These two are only required to make the compiler
            [medal-count-clj.subs]   ;; load them (see docs/Basic-App-Structure.md)
            [medal-count-clj.views])
  (:import [goog History]
           [goog.history EventType]))

;; -- Debugging aids ----------------------------------------------------------
(devtools/install!)       ;; we love https://github.com/binaryage/cljs-devtools
(enable-console-print!)   ;; so that println writes to `console.log`

;; -- Routes and History ------------------------------------------------------
(defn- dispatch-route [match]
  (case (:handler match)
    nil ()
    :index (dispatch [:set-sort :gold])
    :sort-by (dispatch [:set-sort (keyword (-> match :route-params :sort-type))])))


(def history
  (doto (History.)
    (events/listen EventType.NAVIGATE
                   (fn [event]
                     (let [path (.-token event)
                           match (bidi/match-route app-routes path)]
                       (.log js/console event)
                       (.log js/console "path =>" path)
                       (.log js/console "match =>" match)
                       (dispatch-route match)
                     )))
    (.setEnabled true)))



;; -- Entry Point -------------------------------------------------------------
;; Within ../../resources/public/index.html you'll see this code
;;    window.onload = function () {
;;      todomvc.core.main();
;;    }
;; So this is the entry function that kicks off the app once the HTML is loaded.
;;
(defn ^:export main
  []
  ;; Put an initial value into app-db.
  ;; The event handler for `:initialise-db` can be found in `events.cljs`
  ;; Using the sync version of dispatch means that value is in
  ;; place before we go onto the next step.
  (dispatch-sync [:initialise-db])

  ;; Render the UI into the HTML's <div id="app" /> element
  ;; The view function `todomvc.views/todo-app` is the
  ;; root view for the entire UI.
  (reagent/render [medal-count-clj.views/medal-count-clj-app]
                  (.getElementById js/document "app")))
