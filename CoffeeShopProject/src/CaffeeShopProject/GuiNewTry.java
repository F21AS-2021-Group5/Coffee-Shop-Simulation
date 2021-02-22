package CaffeeShopProject;
import javax.swing.*;  
public class GuiNewTry {

public static void main(String[] args) {  
JFrame f=new JFrame();//creating instance of JFrame  
          
JButton b=new JButton("click");//creating instance of JButton  
b.setBounds(130,50,100, 40);//x axis, y axis, width, height 
JButton c=new JButton("click");//creating instance of JButton  
JButton d=new JButton("click");//creating instance of JButton  
JButton e=new JButton("click");//creating instance of JButton  
c.setBounds(130,100,100, 40);//x axis, y axis, width, height  
d.setBounds(130,150,100, 40);//x axis, y axis, width, height  
e.setBounds(130,200,100, 40);//x axis, y axis, width, height  
          
f.add(b);//adding button in JFrame  
f.add(c);
f.add(d);
f.add(e);
          
f.setSize(400,500);//400 width and 500 height  
f.setLayout(null);//using no layout managers  
f.setVisible(true);//making the frame visible  
}  
}  
