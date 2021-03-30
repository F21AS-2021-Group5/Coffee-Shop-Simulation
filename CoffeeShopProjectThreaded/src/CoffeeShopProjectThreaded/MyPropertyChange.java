package CoffeeShopProjectThreaded;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.LinkedList;
 
public class MyPropertyChange {
 
    public static void main(String[] args) throws Exception {
         
    	CoffeeShop cs = new CoffeeShop();
        NewCustomerQueue bean = new NewCustomerQueue(false);  
        
        bean.addPropertyChangeListener(new MyPropertyChangeListener());
        Deque<Customer> queue  =new LinkedList<Customer>();
        Customer custo = new Customer("Adam", LocalDateTime.now());
        //queue.add(custo);
        
        //bean.setMessage(queue, "test1");
        bean.setMessage(new Customer("sdsdsds", LocalDateTime.now()), custo, "added");
        bean.setMessage(3, 10);
        //Deque<Customer> queue1  =new LinkedList<Customer>();
        
        //queue1.add(new Customer("hdhsd", LocalDateTime.now()));
        bean.setMessage(new Customer("sdsdsds", LocalDateTime.now()), new Customer("Ted", LocalDateTime.now()), "removed");
        bean.setMessage(1000, 0);
        
        //bean.setMessage(queue1, "test2");
         
    }
     
    public static class MyPropertyChangeListener implements PropertyChangeListener {
        // This method is called every time the property value is changed
        public void propertyChange(PropertyChangeEvent evt) {
            System.out.println("Name = " + evt.getPropertyName());
            
            if (!evt.getPropertyName().equals("updated queue size"))
            {
                System.out.println("Old Value = " + ((Customer) evt.getOldValue()).getName());
                
                System.out.println("New Value = " + ((Customer) evt.getNewValue()).getName());
         
                System.out.println("**********************************");
            }else {
                System.out.println("Old Value = " + ((int) evt.getOldValue()));
                
                System.out.println("New Value = " + ((int) evt.getNewValue()));
         
                System.out.println("**********************************");
            }
     

        }
    }
     
    public static class Bean {
         
        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
         
        // Property property1
        private String property1;
        // Property property2
        private int property2;
         
        public String getProperty1() {
            return property1;
        }
        public void setProperty1(String property1) {
            pcs.firePropertyChange("property1", this.property1, property1);
            this.property1 = property1;
        }
         
        public int getProperty2() {
            return property2;
        }
        public void setProperty2(int property2) {
            pcs.firePropertyChange("property2", this.property2, property2);
            
            this.property2 = property2;
        }
         
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }
         
    }
 
}
