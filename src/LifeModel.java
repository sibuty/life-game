import java.util.Arrays;

/**
 * ������ ������ "�����".
 * ���� ��������� � ������, �.�. � ����� � ������ ����, � ������� � ������, �������� ����������.
 * ��� ��������� ������������ ������� ������� �����������: ������ ������� �� �������� ������� mainField, ����� �������
 * ��������� ������������ �� ��������������� ������ backField. �� ��������� ������� ������ ���� ������ �� ��� �������
 * �������� �������.
 * � �������� �������� ��������: 0, ���� ������ ������, � 1, ���� ����.
 */
public class LifeModel {

    private byte[] mainField = null;
    private byte[] backField = null;

    private int width, height;
    private int[] neighborOffsets = null;
    private int[][] neighborXYOffsets = null;

    /**
     * ������������� ������.
     *
     * @param width  ������ ���� ������
     * @param height ������ ���� ������
     */
    public LifeModel(int width, int height) {
        this.width = width;
        this.height = height;
        mainField = new byte[width * height];
        backField = new byte[width * height];
        neighborOffsets = new int[]{-width - 1, -width, -width + 1, -1, 1, width - 1, width, width + 1};
        neighborXYOffsets = new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void clear() {
        Arrays.fill(mainField, (byte) 0);
    }

    public void setCell(int x, int y, byte c) {
        mainField[y * width + x] = c;
    }

    public byte getCell(int x, int y) {
        return mainField[y * width + x];
    }

    /**
     * ���� ��� ���������.
     */
    public void simulate() {
        // ������������ ������, �� ���������� ����� ����
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int j = y * width + x;
                byte n = countNeighbors(j);
                backField[j] = simulateCell(mainField[j], n);
            }
        }

        // ������������ ��������� ������
        // ������� � ������ ������
        for (int x = 0; x < width; x++) {
            int j = width * (height - 1);
            byte n = countBorderNeighbors(x, 0);
            backField[x] = simulateCell(mainField[x], n);
            n = countBorderNeighbors(x, height - 1);
            backField[x + j] = simulateCell(mainField[x + j], n);
        }
        // ������� ����� � ������ �������
        for (int y = 1; y < height - 1; y++) {
            int j = width * y;
            byte n = countBorderNeighbors(0, y);
            backField[j] = simulateCell(mainField[j], n);
            n = countBorderNeighbors(width - 1, y);
            backField[j + width - 1] = simulateCell(mainField[j + width - 1], n);
        }

        // ���������� ���� �������
        byte[] t = mainField;
        mainField = backField;
        backField = t;
    }

    /**
     * ������� ������� ��� �� ���������� ����� ������.
     *
     * @param j �������� ������ � �������
     * @return ���-�� �������
     */
    private byte countNeighbors(int j) {
        byte n = 0;
        for (int i = 0; i < 8; i++) {
            n += mainField[j + neighborOffsets[i]];
        }
        return n;
    }

    /**
     * ������� ������� ��� ��������� ������.
     *
     * @param x
     * @param y
     * @return ���-�� �������
     */
    private byte countBorderNeighbors(int x, int y) {
        byte n = 0;
        for (int i = 0; i < 8; i++) {
            int bx = (x + neighborXYOffsets[i][0] + width) % width;
            int by = (y + neighborXYOffsets[i][1] + height) % height;
            n += mainField[by * width + bx];
        }
        return n;
    }

    /**
     * ��������� ��� ����� ������.
     *
     * @param self      ����������� ��������� ������: 0/1
     * @param neighbors ���-�� �������
     * @return ����� ��������� ������: 0/1
     */
    private byte simulateCell(byte self, byte neighbors) {
        return (byte) (self == 0 ? (neighbors == 3 ? 1 : 0) : neighbors == 2 || neighbors == 3 ? 1 : 0);
    }
}