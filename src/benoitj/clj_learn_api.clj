(ns benoitj.clj-learn-api
  (:gen-class)
  (:require
   [compojure.core :refer [defroutes context GET DELETE POST]]
   [compojure.route :as route]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.defaults :as ring-defaults]
   [muuntaja.middleware :as muuntaja]
   [benoitj.api-users :as api-users]
   ))

(defonce server (atom nil))

(defroutes routes
  (GET "/" [] {:body {:response "OK"}})
  (context "/users" []
           (GET "/" [] {:body (api-users/get-users)})
           (GET "/:id" [id] {:body (api-users/get-user (Long/parseLong id))})
           (POST "/" {user :body-params} {:body (api-users/add-user user)})
           (DELETE "/:id" [id] {:body (api-users/del-user (Long/parseLong id))}))
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

