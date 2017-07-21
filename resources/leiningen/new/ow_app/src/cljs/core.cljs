(ns {{name}}.core
  (:require [re-frame.core :as rf]
            [{{name}}.events]))

(enable-console-print!)

(defonce initial-state
  {:foo "bar"})

(rf/reg-event-db :initialize
                 (fn [db _]
                   (merge db initial-state)))

(defn ^:export run []
  (rf/dispatch-sync [:initialize])
  (rf/dispatch [:foo-event :bar]))

(run)
