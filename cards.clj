(ns pairing.cards)

(defn card [value suit]
  {:suit suit :value value})

(defn hand [& cards]
  (set (map (partial apply card) cards)))

(def ^:private point-values {:ace 14 :king 13 :queen 12 :jack 11})

(defn- points [card]
  (or (clojure.core/get point-values (:value card))
      (:value card)))

(defn- sets-of [hand n]
  (filter #(>= (count %1) n) (vals (group-by :value hand))))

(defn- set-of [hand n]
  (not (empty? (sets-of hand n))))

(defn- pair? [hand]
  (when (set-of hand 2)
    "Pair"))

(defn- two-pair? [hand]
  (when (and (set-of hand 2)
       		   (>= (count (sets-of hand 2)) 2))
    "Two pair"))

(defn- three-of-a-kind? [hand]
  (when (set-of hand 3)
    "Three of a kind"))

(defn- straight? [hand]
  (let [values (sort (map points hand))
        least  (apply min values)]
    (when (= values (range least (+ least 5)))
       "Straight")))

(defn- flush? [hand]
  (when (apply = (map :suit hand))
    "Flush"))

(defn- full-house? [hand]
  (when (and (set-of hand 3)
       		   (set-of hand 2)
	           (not= (sets-of hand 2) (sets-of hand 3)))
    "Full house"))

(defn- four-of-a-kind? [hand]
  (when (set-of hand 4)
    "Four of a kind"))

(defn- straight-flush? [hand]
  (when (and (straight? hand)
       	     (flush? hand))
    "Straight flush"))

(def ^:private matchers
  [#'straight-flush? #'four-of-a-kind? #'full-house? #'flush? #'straight? #'three-of-a-kind?  #'two-pair? #'pair?])

(defn rank [hand]
  (some #(apply %1 (list hand)) matchers))
