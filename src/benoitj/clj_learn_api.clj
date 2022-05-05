(ns benoitj.clj-learn-api
  (:gen-class)
  (:require
   [compojure.core :refer [defroutes GET]]
   [ring.adapter.jetty :as jetty]
   [compojure.route :as route]))

(defroutes app
  (GET "/" [] "<h1>Hello World</h1>")
  (route/not-found "<h1>Page not found</h1>"))

(defn -main
  [& args]
  (jetty/run-jetty app {:port 3000}))
