(ns benoitj.api-users)

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



(comment
  (add-user {:id 7 :name "foobar"})
  (add-user {:id 7 :name "not working"})
  (add-user {:id 4 :name "foobaz"})
  (del-user 1)
  (get-user 2)
  (get-users)
  @users)
