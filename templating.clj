(ns pairing.templating)

(defn- expand [f env]
  (apply f (list env)))

(defn- node [& children]
  (fn [env]
    (reduce #(str %1 (expand %2 env)) "" children)))

(defn- string [s]
  (fn [env] s))

(defn- subst [id]
  (fn [env] ((keyword id) env)))

(defn- parse [s & buf]
  (let [buf (first (or buf [""]))
       values (clojure.string/split s #"\$\{|\}")]
    (apply node (map #(%1 %2) (cycle [string subst]) values))))

(defn generate [str env]
  ((parse str) env))
