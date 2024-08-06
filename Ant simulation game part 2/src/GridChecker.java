
/*
    Author: Andrew Whenham
    Purpose: interface will be responsible for checking the grid for occupation, if its a house cell and to just get the cell at a
    specific x,y coordinate.
    AUID: 3469950
 */
public interface GridChecker {
     boolean isCellOccupied(int x, int y);
     boolean isHouseCell(int x, int y);
     Cell getCellAt(int x, int y);

}
