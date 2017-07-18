(ns {{name}}.core)

#?(:cljs (enable-console-print!))

(defn hello-cljc []
  #?(:cljs (println "hello cljc on client")
     :clj  (println "hello cljc on server")))
