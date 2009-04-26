import waba.ui.*;
import waba.fx.*;
import waba.sys.*;
import waba.util.*;
import waba.io.*;

/*
TODO:
variable grid size 
persistence: store state
levels 
*/

public class Knight_bw extends MainWindow {

    static final int N_CELLS = 8;
    int CELL_SIZE = 120 / N_CELLS;
    static final int LABELHEIGHT = 15;
    static final int BUTTONHEIGHT = 15;
    static final int BUTTONWIDTH  = 39;
    static final int ON = 1;
    static final int OFF = 0;
    static final String CATALOGNAME=new String("knVk.DATA");
    
    int cells[][];
    Stack moves;
    
    Graphics g;
    
    Button backButton, clearButton;
    Label leftLabel,waysLabel;
    Font plainFont = new Font("Helvetica", Font.PLAIN, 12);
    int n_left,n_ways;

    public Knight_bw() {
	
  	g = new Graphics(this);
	cells = new int[N_CELLS][N_CELLS];
	moves = new Stack();
	

	g.setColor(255,255,255);
	Rect rect = getRect();
	g.fillRect(0,0,width,height);
	g.setColor(0,0,0);
	
	backButton = new Button("Back");
	backButton.setRect(0, height - BUTTONHEIGHT, BUTTONWIDTH, BUTTONHEIGHT);
	add(backButton);
	
	clearButton = new Button("Clear");
	clearButton.setRect(BUTTONWIDTH + 1 , height - BUTTONHEIGHT, BUTTONWIDTH, BUTTONHEIGHT);
	add(clearButton);

	leftLabel = new Label("left");
	leftLabel.setRect(18,0,15,10);
	add(leftLabel);
	waysLabel = new Label("ways");
	waysLabel.setRect(62,0,20,10);
	add(waysLabel);

	n_left = N_CELLS*N_CELLS;
	if(CELL_SIZE%2!=0)
	    CELL_SIZE--;
	loadState();
    }
    
    public void onPaint(Graphics g)
    {
	g.setColor(0,0,0);
	g.drawRect(4,LABELHEIGHT+4,N_CELLS*CELL_SIZE+1,N_CELLS*CELL_SIZE+1);
	for(int i=0; i< N_CELLS; i++)
	    {
		g.drawLine(4+i*CELL_SIZE, LABELHEIGHT+4, 4+i*CELL_SIZE, LABELHEIGHT+N_CELLS*CELL_SIZE+3);
		g.drawLine(4, LABELHEIGHT+4+i*CELL_SIZE, N_CELLS*CELL_SIZE+3, LABELHEIGHT+4+i*CELL_SIZE);
		for (int j=0; j<N_CELLS; j++)
		    {
			if (cells[i][j]==ON)
			    fillCell(i,j);
		    }
	    }
	
    }
    

    public void onEvent(Event event)
    {
 
	if (event.type == PenEvent.PEN_DOWN)
	    { 		
		if (event.target == backButton){
		    goBack();
		}
		else if(event.target == clearButton){
		    cells = new int[N_CELLS][N_CELLS];
		    moves=new Stack();
		    n_left = N_CELLS*N_CELLS;
		    repaint();
		}
		else{
		    PenEvent pen = (PenEvent)event;
		    int x = (pen.x-4)/CELL_SIZE;
		    int y = (pen.y-LABELHEIGHT-4)/CELL_SIZE;
		    if(x>=0 && y>=0 && x<N_CELLS && y<N_CELLS && cells[x][y]!=ON && validMove(x,y)){
			paintCell(x,y);
		    }
		}
	    }

    }
    private void clearCell(int x, int y){
	g.setColor(255,255,255);
	g.fillRect(5+x*CELL_SIZE, LABELHEIGHT+5+y*CELL_SIZE, CELL_SIZE-1, CELL_SIZE-1);
    }
    private void fillCell(int x, int y){
	g.setColor(0,0,0);
	int inc=2;
	for(int i = LABELHEIGHT+6+y*CELL_SIZE; i < LABELHEIGHT+5+(y+1)*CELL_SIZE; i+=inc)
	    g.drawDots(6+x*CELL_SIZE,i,3+(x+1)*CELL_SIZE,i);
    }
    private void markCell(int x, int y){
	g.setColor(0,0,0);
	int inc=2;
	for(int i = LABELHEIGHT+6+y*CELL_SIZE; i < LABELHEIGHT+5+(y+1)*CELL_SIZE; i+=inc)
	    g.drawLine(6+x*CELL_SIZE,i,2+(x+1)*CELL_SIZE,i);
    }

    private void drawLeft()
    {
	g.setColor(0,0,0);
	g.drawText(String.valueOf(n_left),0,0);	
    }
    private void clearLeft()
    {
	g.setColor(255,255,255);
	g.fillRect(0,0,18,12);
    }
    private void drawWays()
    {
	g.setColor(0,0,0);
	g.drawText(String.valueOf(n_ways),50,0);	
	if(n_ways==0 && n_left==0)
	    g.drawText("SUCCESS",90,0);
	else if(n_ways==0)
	    g.drawText("DEADLOCK",90,0);	
	else{
	    g.setColor(255,255,255);
	    g.fillRect(90,0,50,12);
	}
    }
    private void clearWays()
    {
	g.setColor(255,255,255);
	g.fillRect(50,0,12,12);
    }
    private int countWays(int x, int y)
    {
	int w = 0;

	if(x<N_CELLS-2 && y<N_CELLS-1 && cells[x+2][y+1]==OFF)
	    w++;
	if(x<N_CELLS-2 && y>=1 && cells[x+2][y-1]==OFF)
	    w++;
	if(x>=2 && y<N_CELLS-1 && cells[x-2][y+1]==OFF)
	    w++;
	if(x>=2 && y>=1 && cells[x-2][y-1]==OFF)
	    w++;
	if(x<N_CELLS-1 && y<N_CELLS-2 && cells[x+1][y+2]==OFF)
	    w++;
	if(x<N_CELLS-1 && y>=2 && cells[x+1][y-2]==OFF)
	    w++;
	if(x>=1 && y<N_CELLS-2 && cells[x-1][y+2]==OFF)
	    w++;
	if(x>=1 && y>=2 && cells[x-1][y-2]==OFF)
	    w++;

	return w;
    }

    public void goBack()
    {
	int len = moves.length();
	if(len>0){
	    clearLeft();
	    n_left++;
	    drawLeft();
	    /*Can i add this to the start of the list??
	     or implement my own stack??*/
	    Coord lastmove = (Coord)moves.top();
	    clearCell(lastmove.x(), lastmove.y());
	    cells[lastmove.x()][lastmove.y()]=OFF;
	    moves.pop();
	    if(len>1){
		lastmove = (Coord)moves.top();
		clearCell(lastmove.x(), lastmove.y());
		markCell(lastmove.x(), lastmove.y());
	    }
	    clearWays();
	    if(len>1){
		n_ways = lastmove.z();//countWays(lastmove.x(),lastmove.y());
		drawWays();
	    }
	}
	else{
	    clearWays();
	}
	    
    }

    public void paintCell(int x, int y)
    {
	if(moves.length()>0){
	    Coord lastmove = (Coord)moves.top();
	    clearCell(lastmove.x(), lastmove.y());
	    fillCell(lastmove.x(), lastmove.y());
	}	    
	markCell(x,y);
	cells[x][y]=ON;
	clearLeft();
	n_left--;
	drawLeft();
	clearWays();
	n_ways = countWays(x,y);
	drawWays();
	Coord currmove = new Coord(x,y,n_ways);
	moves.push(currmove);
	
    }

    private boolean validMove(int x, int y)
    {
	if(moves.length()==0)
	    return true;
	Coord lastmove = (Coord)moves.top();
	int currx=lastmove.x();
	int curry=lastmove.y();
	if(x==currx+2 && (y==curry-1 || y==curry+1))
	   return true;
	else  if(x==currx-2 && (y==curry-1 || y==curry+1))
	    return true;
	else if(y==curry+2 && (x==currx-1 || x==currx+1))
	    return true;
	else if(y==curry-2 && (x==currx-1 || x==currx+1))
	    return true;
	else
	    return false;
    }
    public void onExit(){
	saveState();
    }
    public void saveState(){
	/*need to store the board state and the moves array
	  maybe should just store level number

        Catalog c=new Catalog(CATALOGNAME,Catalog.CREATE);
	if(!c.isOpen()){
	    return;
	}
	if(c.getRecordCount()!=N_CELLS*N_CELLS){
	    c.addRecord(N_CELLS*N_CELLS);
	}
	c.setRecordPos(0);
	byte[] b=new byte[N_CELLS*N_CELLS];
	int i=0;
	for(byte x=0;x<N_CELLS;x++){
	    for(byte y=0;y<N_CELLS;y++){
		if(cells[x][y]==ON){
		    b[i]=1;
		}
		else{
		    b[i]=0;
		}
		i++;
	    }
	}
	c.writeBytes(b,0,N_CELLS*N_CELLS);
	c.close();
	*/   }
    public void loadState(){
	/*	Catalog c=new Catalog(CATALOGNAME,Catalog.READ_ONLY);
	if(!c.isOpen() || c.getRecordCount()==0 || !c.setRecordPos(0)){
	    return;
	}
	byte[] b=new byte[N_CELLS*N_CELLS];
	c.readBytes(b,0,N_CELLS*N_CELLS);
	c.close();
	int i=0;
	for(byte x=0;x<N_CELLS;x++){
	    for(byte y=0;y<N_CELLS;y++){
		if(b[i]==1){
		    cells[x][y]=ON;
		}
		else{
		    cells[x][y]=OFF;
		}
		i++;
	    }
	    }*/
    }

}
