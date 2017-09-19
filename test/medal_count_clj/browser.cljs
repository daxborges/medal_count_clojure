(ns medal-count-clj.browser
  (:require [doo.runner :refer-macros [doo-tests]]
            [medal-count-clj.core-test]))

(doo-tests 'medal-count-clj.core-test)