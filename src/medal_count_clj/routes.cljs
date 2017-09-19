(ns medal-count-clj.routes
  (:require
    [bidi.bidi :as bidi]))


(def app-routes ["/" {"" :index
                      "sort-by/" { [:sort-type ""] :sort-by }
                      }])


(defn path-for
  "Builds path for valid routes"
  [& args]
  (apply bidi/path-for app-routes args))

;; -- URL  ----------------------------------------------------------
(defn ->navigate-path
  "Navigates to a path"
  [path]
  (.href js/location (str (.host js/location) "/" path)))