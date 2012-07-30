(require '[clojure.string :as string])

(defn- size [n]
  (-> n Math/sqrt Math/floor int))

(defn- radius [n]
  (-> n size (+ 1) (quot 2)))

(defn- theta [n]
  (- n (int (Math/pow (- (* (radius n) 2) 1) 2.0))))

(defn- quadrant [n]
  (quot (theta n) (* 2 (radius n))))

(defn- offset [n]
  (mod (theta n) (* 2 (radius n))))

(defn- base [n]
  [(radius n) (+ (- 1 (radius n)) (offset n))])

(defn- transform [n]
 (reduce (fn [[x y] _] [(- y) x]) (base n) (range 0 (quadrant n))))

(defn- center [n]
  (if (= n 0)
    [0 0]
    (let [center (+ (- (radius n) 1) (quot (quadrant n) 2))]
      [center center])))

(defn- absolute [s n]
  (vec (map + (center s) (transform n))))

(defn- starter [n]
  {(center n) 0})

(defn- add [s spiral n]
  (let [pos (absolute s n)]
	  (conj spiral {pos n})))

(defn- build [n]
  (reduce (partial add n) (starter n) (range 1 (+ n 1))))

(defn spiral [n]
  (let [spiral (build n)
        digits (count (str n))
        fmt    (str "% " (+ digits 1) "d")
        filler (apply str (map #(do %1 " ") (range (+ digits 1))))
        rng    (range 0 (+ (size n) 1))]
    (string/join "\n"
      (for [y rng]
        (string/join " "
          (for [x rng]
      			(let [n (get spiral [x y])]
                    (if n
                      (format fmt n)
                      filler))))))))


