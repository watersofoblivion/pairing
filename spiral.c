#include <stdio.h>
#include <math.h>
#include <stdlib.h>

#define SIZE 1500

#define RIGHT 0
#define DOWN 1
#define LEFT 2
#define UP 3

int main(int, char**);
void spiral(int);
void move(int, int*, int*);

int main(int argc, char** argv) {
  int n;
  
  for (n = 0; n < SIZE; n++) {
    spiral(n);
    printf("\n");
    fflush(stdout);
  }
}

void spiral(int n) {
  int i;
  int direction = UP;
  int temp_x, temp_y, temp_direction;
  int size = (int)floor(sqrt(n)) + 1;
  int center = size / 2 - (size + 1) % 2;
  int x = center;
  int y = center;
  int* spiral = (int*)calloc(size * size, sizeof(int));
  
  spiral[center * size + center] = 1;
  for (i = 1; i <= n; i++) {
    temp_direction = (direction + 1) % 4;
    temp_x = x;
    temp_y = y;

    move(temp_direction, &temp_x, &temp_y);

    if (spiral[temp_y * size + temp_x]) {
      move(direction, &x, &y);
    } else {
      direction = temp_direction;
      x = temp_x;
      y = temp_y;
    }
    
    spiral[y * size + x] = i;
  }
  spiral[center * size + center] = 0;
  
  for (y = 0; y < size; y++) {
    for (x = 0; x < size; x++) {
      i = spiral[y * size + x];
      if (i || (x == center && y == center)) {
        printf("% 5d", i);
      } else {
        printf("     ");
      }
      printf(" ");
    }
    printf("\n");
  }
  
  free(spiral);
}

void move(int direction, int* x, int* y) {
  switch (direction) {
    case RIGHT:
      *x = *x + 1;
      break;
    case DOWN:
      *y = *y + 1;
      break;
    case LEFT:
      *x = *x - 1;
      break;
    case UP:
      *y = *y - 1;
      break;
  }
}

