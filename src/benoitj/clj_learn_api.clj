(ns benoitj.clj-learn-api
  (:gen-class)
  (:require
   [compojure.core :refer [defroutes context GET DELETE POST]]
   [compojure.route :as route]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.defaults :as ring-defaults]
   [muuntaja.middleware :as muuntaja]))

(defonce server (atom nil))

(def users (atom {1 {:id 1 :name "John"}
                  2 {:id 2 :name "Jane"}}))

(defn get-users []
  (into [] (map second @users)))

(defn get-user [id]
  (get @users id))

(defn add-user [user]
  (when (not (@users (:id user)))
    (swap! users #(assoc % (:id user) user))
    user))

(defn del-user [id]
  (let [del-record (@users id)]
    (swap! users #(dissoc % id))
    del-record))

(defroutes routes
  (GET "/" [] {:body {:response "OK"}})
  (context "/users" []
           (GET "/" [] {:body (get-users)})
           (GET "/:id" [id] {:body (get-user (Long/parseLong id))})
           (POST "/" {user :body-params} {:body (add-user user)})
           (DELETE "/:id" [id] {:body (del-user (Long/parseLong id))}))
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
  (stop-jetty!)
  (add-user {:id 7 :name "foobar"})
  (add-user {:id 7 :name "not working"})
  (add-user {:id 4 :name "foobaz"})
  (del-user 1)
  (get-user 2)
  (get-users)
  @users
  )

