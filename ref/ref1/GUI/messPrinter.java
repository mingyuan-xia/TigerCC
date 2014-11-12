package GUI;

public interface messPrinter {
abstract void printErr(String s);
abstract void printMes(String s);

abstract void printErr(int s);
abstract void printMes(int s);

abstract void printErr(Error s);
}
