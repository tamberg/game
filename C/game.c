#include <stdio.h>
#include <unistd.h>
#include <assert.h>

int pot(int b, int n) {
    int res = 1;
    while (n > 0) {
        res = res * b;
        n--;
    }
    return res;
}

int ask(char *name, int lower, int upper) {
    printf("%s (%d-%d) = ", name, lower, upper);
    int i;
    scanf("%d", &i);
    return i;
}

int move(int board, int c, int n) {
    int d = pot(10, c) * n;
    return board - d;
}

int digit(int board, int c) {
    int m = pot(10, c + 1);
    int d = pot(10, c);
    return (board % m) / d;
}

int wins(int board) {
    return
        board ==      1 ||
        board ==     10 ||
        board ==    100 ||
        board ==   1000 ||
        board ==  10000 ||
        board == 100000;
}

int loses(int board) {
    return board == 0;
}

int other(int team) {
    return (team + 1) % 2;
}

int main(void) {
    int board = 356653;
    int team = 0;
    int done;
    do {
        printf("team %d plays\n", team);
        printf("board %06d\n", board);
        int c, d, c_valid, d_valid;
        do {
            do {
                c = 6 - ask("c", 1, 6); // inverted
                c_valid = 0 <= c && c < 6;
            } while (!c_valid);
            d = digit(board, c);
            d_valid = d > 0;
        } while (!d_valid);
        int n, n_valid; 
        do {
            n = ask("n", 1, d);
            n_valid = 0 < n && n <= d;
        } while (!n_valid);
        printf("\n");
        board = move(board, c, n);
        done = wins(board) || loses(board);
        if (!done || loses(board)) {
            team = other(team);
        }
    } while (!done);
    printf("team %d wins!\n", team);
    printf("board %06d :)\n", board);
    return 0;
}
