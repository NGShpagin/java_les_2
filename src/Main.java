import java.util.Random;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static int WIN_COUNT;
    private static int xTurn;
    private static int yTurn;
    private static final char DOT_HUMAN = 'x';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';
    private static final Scanner SCANNER = new Scanner(System.in);
    private static char[][] field; // Двумерный массив хранит текущее состояние игрового поля
    private static int fieldSizeX; // Размерность игрового поля
    private static int fieldSizeY; // Размерность игрового поля
    private static final Random random = new Random();


    public static void main(String[] args) {
        do {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (gameCheck(DOT_HUMAN, "Вы победили!")) break;
                if (!checkHumWin(WIN_COUNT - 1)) aiTurn();
                printField();
                if (gameCheck(DOT_AI, "AI победил!")) break;
            }
            System.out.println("Желаете сыграть еще раз? (Y - да, N - нет)");
        } while (SCANNER.next().equalsIgnoreCase("Y"));

    }

    /**
     * Инициализация игрового поля
     */
    private static void initialize() {
        // Установим размерность игрового поля
        System.out.print("Введите размерность поля по горизонтали: ");
        fieldSizeX = SCANNER.nextInt();
        System.out.print("Введите размерность поля по вертикали: ");
        fieldSizeY = SCANNER.nextInt();
        int min = Math.min(fieldSizeX, fieldSizeY);
        System.out.print("Введите кол-во символов подряд для победы (не менее 1 и не более " + min + "): ");
        WIN_COUNT = SCANNER.nextInt();

        field = new char[fieldSizeX][fieldSizeY];
        // Пройдем по всем элементам массива
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeX; y++) {
                // Проинициализируем все элементы массива DOT_EMPTY (признак пустого поля)
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Отрисовка игрового поля
     * TODO: Поправить отрисовку игрового поля
     */
    private static void printField() {
        System.out.print("+");
        for (int i = 0; i < fieldSizeX * 2 + 1; i++) {
            System.out.print((i % 2 == 0) ? "-" : i / 2 + 1);
        }
        System.out.println();

        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print(i + 1 + "|");

            for (int j = 0; j < fieldSizeY; j++) {
                System.out.print(field[i][j] + "|");
            }

            System.out.println();
        }

        for (int i = 0; i < fieldSizeX * 2 + 2; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Отработка хода игрока (человек)
     */
    private static void humanTurn() {
        int x, y;
        do {
            System.out.println("Введите координаты хода X и Y (от 1 до 3) через пробел");
            System.out.print(">>> ");
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
        xTurn = x;
        yTurn = y;
    }

    /**
     * Ход AI
     */
    private static void aiTurn() {
        int x, y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
        xTurn = x;
        yTurn = y;
    }

    /**
     * Проверки ячейки на пустоту
     *
     * @param x
     * @param y
     * @return
     */
    static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка корректности ввода
     * (координаты хода не должны превышать размерность массива, игрового поля)
     *
     * @param x
     * @param y
     * @return
     */
    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Проверка победы
     * TODO: Переработать метод в домашнем задании
     *
     * @return
     */
    static boolean checkWin(char c) {

        // Проверка по вертикалям
        int count = 0;
        for (int y = 0; y < fieldSizeY; y++) {
            if (field[xTurn][y] == c) {
                count++;
                if (count == Main.WIN_COUNT) return true;
            } else count = 0;
        }

        // Проверка по горизонталям
        count = 0;
        for (int x = 0; x < fieldSizeX; x++) {
            if (field[x][yTurn] == c) {
                count++;
                if (count == Main.WIN_COUNT) return true;
            } else count = 0;
        }

        // Проверка по диагоналям (сверху слева -> вниз вправо)
        count = 1;
        int i = 1;
        while (xTurn - i >= 0 && yTurn - i >= 0) {
            if (field[xTurn - i][yTurn - i] == c) {
                count++;
                if (count == Main.WIN_COUNT) return true;
                i++;
            } else break;
        }
        i = 1;
        while (xTurn + i < fieldSizeX && yTurn + i < fieldSizeY) {
            if (field[xTurn + i][yTurn + i] == c) {
                count++;
                if (count == Main.WIN_COUNT) return true;
                i++;
            } else break;
        }

        // Проверка по диагоналям (снизу слева -> вверх вправо)
        count = 1;
        i = 1;
        while (xTurn + i < fieldSizeX && yTurn - i >= 0) {
            if (field[xTurn + i][yTurn - i] == c) {
                count++;
                if (count == Main.WIN_COUNT) return true;
                i++;
            } else break;
        }
        i = 1;
        while (xTurn - i >= 0 && yTurn + i < fieldSizeY) {
            if (field[xTurn - i][yTurn + i] == c) {
                count++;
                if (count == Main.WIN_COUNT) return true;
                i++;
            } else break;
        }

        return false;
    }

    static boolean checkHumWin(int winVar) {

        // Проверка по вертикалям
        int count = 0;
        for (int y = 0; y < fieldSizeY; y++) {
            if (field[xTurn][y] == Main.DOT_HUMAN) {
                count++;
                if (count == winVar) {
                    if (y + 1 < fieldSizeY && isCellEmpty(xTurn, y + 1)) {
                        field[xTurn][y + 1] = DOT_AI;
                        return true;
                    } else if (y - winVar > 0 && isCellEmpty(xTurn, y - winVar)) {
                        field[xTurn][y - winVar] = DOT_AI;
                        return true;
                    }
                }
            } else count = 0;
        }

        // Проверка по горизонталям
        count = 0;
        for (int x = 0; x < fieldSizeY; x++) {
            if (field[x][yTurn] == Main.DOT_HUMAN) {
                count++;
                if (count == winVar) {
                    if ((x + 1) < fieldSizeY && isCellEmpty(x + 1, yTurn)) {
                        field[x + 1][yTurn] = DOT_AI;
                        return true;
                    } else if ((x - winVar) > 0 && isCellEmpty(x - winVar, yTurn)) {
                        field[x - winVar][yTurn] = DOT_AI;
                        return true;
                    }
                }
            } else count = 0;
        }

        // Проверка по диагоналям (сверху слева -> вниз вправо)
        count = 1;
        int i = 1;
        while (xTurn - i >= 0 && yTurn - i >= 0) {
            if (field[xTurn - i][yTurn - i] == Main.DOT_HUMAN) {
                count++;
                if (count == winVar) {
                    if (xTurn - i - 1 >= 0 && yTurn - i - 1 >= 0 && isCellEmpty(xTurn - i - 1, yTurn - i - 1)) {
                        field[xTurn - i - 1][yTurn - i - 1] = DOT_AI;
                        return true;
                    } else if ((xTurn + winVar) < fieldSizeX && (yTurn + winVar) < fieldSizeY && isCellEmpty(xTurn + winVar, yTurn + winVar)) {
                        field[xTurn + winVar][yTurn + winVar] = DOT_AI;
                        return true;
                    }
                }
                i++;
            } else break;
        }

        while (xTurn + i < fieldSizeX && yTurn + i < fieldSizeY) {
            if (field[xTurn + i][yTurn + i] == Main.DOT_HUMAN) {
                count++;
                if (count == winVar) {
                    if ((xTurn + i + 1) < fieldSizeX && (yTurn + i + 1) < fieldSizeY && isCellEmpty(xTurn + i + 1, yTurn + i + 1)) {
                        field[xTurn + i + 1][yTurn + i + 1] = DOT_AI;
                        return true;
                    } else if ((xTurn - winVar) >= 0 && (yTurn - winVar) >= 0 && isCellEmpty(xTurn - winVar, yTurn - winVar)) {
                        field[xTurn - winVar][yTurn - winVar] = DOT_AI;
                        return true;
                    }
                }
                i++;
            } else break;
        }

        // Проверка по диагоналям (снизу слева -> вверх вправо)
        count = 1;
        i = 1;
        while (xTurn + i < fieldSizeX && yTurn - i >= 0) {
            if (field[xTurn + i][yTurn - i] == Main.DOT_HUMAN) {
                count++;
                if (count == winVar) {
                    if (xTurn + i + 1 < fieldSizeX && yTurn - i - 1 >= 0 && isCellEmpty(xTurn + i + 1, yTurn - i - 1)) {
                        field[xTurn + i + 1][yTurn - i - 1] = DOT_AI;
                        return true;
                    } else if ((xTurn - winVar) >= 0 && (yTurn + winVar) < fieldSizeY && isCellEmpty(xTurn - winVar, yTurn + winVar)) {
                        field[xTurn - winVar][yTurn + winVar] = DOT_AI;
                        return true;
                    }
                }
                i++;
            } else break;
        }

        while (xTurn - i >= 0 && yTurn + i < fieldSizeY) {
            if (field[xTurn - i][yTurn + i] == Main.DOT_HUMAN) {
                count++;
                if (count == winVar) {
                    if ((xTurn - i - 1) >= 0 && (yTurn + i + 1) < fieldSizeY && isCellEmpty(xTurn - i - 1, yTurn + i + 1)) {
                        field[xTurn - i - 1][yTurn + i + 1] = DOT_AI;
                        return true;
                    } else if ((xTurn + winVar) < fieldSizeX && (yTurn - winVar) >= 0 && isCellEmpty(xTurn + winVar, yTurn - winVar)) {
                        field[xTurn + winVar][yTurn - winVar] = DOT_AI;
                        return true;
                    }
                }
                i++;
            } else break;
        }

//        while (xTurn + i < fieldSizeX && yTurn - i >= 0) {
//            if (field[xTurn + i][yTurn - i] == c) {
//                count++;
//                if (count == winVar) return true;
//                i++;
//            } else break;
//        }
//        i = 1;
//        while (xTurn - i >= 0 && yTurn + i < fieldSizeY) {
//            if (field[xTurn - i][yTurn + i] == c) {
//                count++;
//                if (count == winVar) return true;
//                i++;
//            } else break;
//        }

        return false;
    }

    /**
     * Проверка на ничью
     *
     * @return true - ничья / false - нет ничьи
     */
    static boolean checkDraw() {
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++)
                if (isCellEmpty(x, y)) return false;
        }
        return true;
    }

    /**
     * Метод проверки состояния игры
     *
     * @param c
     * @param str - текст победы
     * @return true - стоп игра / false - игра продолжается
     */
    static boolean gameCheck(char c, String str) {
        if (checkWin(c)) {
            System.out.println(str);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }

        return false; // Игра продолжается
    }
}