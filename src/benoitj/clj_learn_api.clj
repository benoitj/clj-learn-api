(ns benoitj.clj-learn-api
  (:gen-class)
  (:require
   [compojure.core :refer [defroutes GET]]
   [compojure.route :as route]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.defaults :as ring-defaults]
   [muuntaja.middleware :as muuntaja]))

(defonce server (atom nil))

(defroutes routes
  (GET "/" [] {:body {:response "OK"}})
  (route/not-found "Page not found"))

(defn start-jetty! [& _]
  (jetty/run-jetty
   (-> #'routes
       muuntaja/wrap-format
       (ring-defaults/wrap-defaults ring-defaults/api-defaults))
   {:join? false :port 3000}))

(defn stop-jetty! [& _]
  (.stop @server))
  

(defn -main [& _]
  (reset! server (start-jetty!)))


(comment
  (-main)
  (stop-jetty!))
