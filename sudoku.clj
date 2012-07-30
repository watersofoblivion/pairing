(ns pairing.sudoku)

(defn- board [width height]
  (let [side  (* width height)
        cell  (set (range 1 (+ 1 side)))
        size  (range 0 side)
        cells (for [x size y size] {[x y] cell})]
    (reduce conj {:width width :height height} cells)))

(defn- side [board]
  (* (:width board) (:height board)))

(defn- indexes [f board]
  (for [x (range 0 (side board))] (f x)))

(defn- row [board [_ row]]
  (indexes #(vector %1 row) board))

(defn- column [board [column _]]
  (indexes #(vector column %1) board))

(defn- neighborhood [board [x y]]
  (let [width  (:width board)
        height (:height board)
        x      (* width (quot x width))
        y      (* height (quot y height))]
    (indexes #(vector (+ x (mod %1 width)) (+ y (quot %1 width))) board)))

(defn- cells [board loc]
  (lazy-cat (row board loc) (column board loc) (neighborhood board loc)))
  
(declare set-cell)

(defn- reduce-cell [board loc value]
  (let [values (get board loc)]
    (if (= (count values) 1)
      board
      (let [values (disj values value)
            board  (conj board {loc values})]
        (if (= (count values) 1)
          (set-cell board loc value)
          board)))))

(defn- reduce-cells [board cells value]
  (reduce #(reduce-cell %1 %2 value) board cells))

(defn- set-cell [board loc value]
  (-> board
      (reduce-cells (cells board loc) value)
      (conj {loc #{value}})))

(defn solve [width height values]
  (let [board (board width height)]
    (reduce (fn [board [loc value]] (set-cell board loc value)) board values)))

(solve 3 3 {[0 0] 5
             [1 0] 3
             [4 0] 7

             [0 1] 6
             [3 1] 1
             [4 1] 9
             [5 1] 5

             [1 2] 9
             [2 2] 8
             [7 2] 2

             [0 3] 8
             [4 3] 6
             [8 3] 3

             [0 4] 4
             [3 4] 8
             [5 4] 3
             [8 4] 1

             [0 5] 7
             [4 5] 2
             [8 5] 6

             [1 6] 6
             [6 6] 2
             [7 6] 8

             [3 7] 4
             [4 7] 1
             [5 7] 9
             [8 7] 5

             [4 8] 8
             [7 8] 7
             [8 8] 9})
