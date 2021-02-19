package CaffeeShopProject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class GUI {

	protected Shell shell;
	private Text txtMenu;
	private Text text;
	private Text txt_tax;
	private Text txt_subt;
	private Text txt_discnt;
	private Text txt_total;
	private Text txtCustomerId;
	
	Customer customer_id;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GUI window = new GUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(800, 700);
		shell.setText("SWT Application");
		
		
		txtMenu = new Text(shell, SWT.BORDER);
		txtMenu.setText("Menu");
		txtMenu.setBounds(10, 10, 51, 26);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(10, 122, 298, 206);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		List list = new List(scrolledComposite, SWT.BORDER);
		scrolledComposite.setContent(list);
		scrolledComposite.setMinSize(list.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		Button btnRemove = new Button(shell, SWT.NONE);
		btnRemove.setBounds(10, 334, 66, 30);
		btnRemove.setText("Remove");
		
		Spinner spinner = new Spinner(shell, SWT.BORDER);
		spinner.setBounds(91, 336, 59, 26);
		
		Button btnAdd = new Button(shell, SWT.NONE);
		btnAdd.setBounds(168, 334, 51, 30);
		btnAdd.setText("Add");
		
		Button btnPurchase = new Button(shell, SWT.NONE);
		btnPurchase.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnPurchase.setBounds(237, 334, 70, 30);
		btnPurchase.setText("Purchase");
		
		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1.setBounds(360, 10, 177, 245);
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);
		
		List list_2 = new List(scrolledComposite_1, SWT.BORDER);
		scrolledComposite_1.setContent(list_2);
		scrolledComposite_1.setMinSize(list_2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		ScrolledComposite scrolledComposite_1_1 = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1_1.setExpandVertical(true);
		scrolledComposite_1_1.setExpandHorizontal(true);
		scrolledComposite_1_1.setBounds(360, 277, 177, 366);
		
		List list_1 = new List(scrolledComposite_1_1, SWT.BORDER);
		scrolledComposite_1_1.setContent(list_1);
		scrolledComposite_1_1.setMinSize(list_1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		ScrolledComposite scrolledComposite_1_2 = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1_2.setExpandVertical(true);
		scrolledComposite_1_2.setExpandHorizontal(true);
		scrolledComposite_1_2.setBounds(559, 10, 213, 633);
		
		List list_3 = new List(scrolledComposite_1_2, SWT.BORDER);
		scrolledComposite_1_2.setContent(list_3);
		scrolledComposite_1_2.setMinSize(list_3.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		text = new Text(shell, SWT.BORDER);
		text.setText("Item Description");
		text.setBounds(10, 378, 297, 60);
		
		txt_tax = new Text(shell, SWT.BORDER);
		txt_tax.setText("Tax");
		txt_tax.setBounds(10, 458, 190, 30);
		
		txt_subt = new Text(shell, SWT.BORDER);
		txt_subt.setText("Subtotal");
		txt_subt.setBounds(10, 513, 190, 30);
		
		txt_discnt = new Text(shell, SWT.BORDER);
		txt_discnt.setText("Discount");
		txt_discnt.setBounds(10, 566, 190, 30);
		
		txt_total = new Text(shell, SWT.BORDER);
		txt_total.setText("Total");
		txt_total.setBounds(10, 613, 190, 30);
		
		Button btnMeal = new Button(shell, SWT.NONE);
		btnMeal.setBounds(10, 90, 66, 26);
		btnMeal.setText("Meal");
		
		Button btnDessert = new Button(shell, SWT.NONE);
		btnDessert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnDessert.setText("Desserts");
		btnDessert.setBounds(168, 90, 66, 26);
		
		Button btnDrink = new Button(shell, SWT.NONE);
		btnDrink.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnDrink.setText("Drinks");
		btnDrink.setBounds(242, 90, 66, 26);
		
		Button btnSide = new Button(shell, SWT.NONE);
		btnSide.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
			}
		});
		btnSide.setText("Sides");
		btnSide.setBounds(82, 90, 66, 26);
		
		Button btnOrderList = new Button(shell, SWT.NONE);
		btnOrderList.setBounds(217, 456, 90, 30);
		btnOrderList.setText("Order List");
		
		Button btnReceipt = new Button(shell, SWT.NONE);
		btnReceipt.setBounds(217, 511, 90, 30);
		btnReceipt.setText("Receipt");
		
		Button btnFinalReportCoffee = new Button(shell, SWT.NONE);
		btnFinalReportCoffee.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnFinalReportCoffee.setBounds(217, 613, 90, 30);
		btnFinalReportCoffee.setText("Final Report");
		DisplayCustomer();

	}
	
	private void DisplayCustomer() {
		Button btnNewCustomer = new Button(shell, SWT.NONE);	// Create New Customer button
		btnNewCustomer.setBounds(198, 10, 109, 26);		// Button size
		btnNewCustomer.setText("New Customer");		// Button text
		// Action New Customer Button
		btnNewCustomer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();		// CHANGE LATER, for now just close window
			}
		});
		
		// Display Current customer and their ID
		txtCustomerId = new Text(shell, SWT.BORDER);
		txtCustomerId.setText("Customer ID" ); //+ customer_id.getId()
		txtCustomerId.setBounds(10, 55, 298, 26);
	}
	
}

