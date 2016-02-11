package com.horstmann.violet.application.help;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
/**
 * Creates dialog menu of available shortcut keys for use in application.
 * @author Mateusz Mucha
 */
public class KeyAssist extends JDialog {
	private static final long serialVersionUID = 6347912754958499764L;
	/**
	 * Array of shortcut keys must contains all shortcut names from property, used as prefix.
	 */
	private static final String[] SHORTCUT_KEYS = { 
			"open", "close", "save", "saveAs", "print", "exit", "undo", "redo", "properties", "cut", "copy", "paste", "delete", "selectAll", 
			"selectNext", "selectPrevious", "zoomOut", "zoomIn", "growDrawingArea", "clipDrawingArea", "smallerGrid", "largerGrid", "shortcuts", 
			"windowNext", "windowPrevious", "openRemote", "exportImage", "exportClipboard", "diagram.class", "diagram.object", "diagram.useCase", 
			"diagram.state", "diagram.activity", "diagram.sequence" 
	};
	@ResourceBundleBean( key = "dialog.title" )
	private String shortcutDialogTitle;
	@ResourceBundleBean( key = "column.keys" )
	private String shortcutColumn;
	@ResourceBundleBean( key = "column.description" )
	private String descriptionColumn;
	@ResourceBundleBean( key="button.title" )
    private String closeButtonTitle;
	
	private JPanel shortcutKeysPanel;
	
	
	public KeyAssist( JFrame parent ) {
		super( parent );
		ResourceBundleInjector.getInjector().inject( this );
		BeanInjector.getInjector().inject( this );
		
		this.setTitle( this.shortcutDialogTitle );
        this.setLocationRelativeTo( null );
        this.setModal( true );
        this.setResizable( false );
        this.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        this.getContentPane().setLayout( new BorderLayout() );

        this.getContentPane().add( keyAssistBuildPanel(), BorderLayout.CENTER );
        this.getContentPane().add( closePanelButton(), BorderLayout.SOUTH );
		
		this.pack();
		this.setLocation( parent );
	}
	/**
	 * Build close panel button.
	 * @return panel of close button
	 */
	private JPanel closePanelButton() {
		JPanel closePanel = new JPanel();
		
		closePanel.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
		closePanel.setLayout( new GridBagLayout() );
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		JButton closeButton = new JButton( this.closeButtonTitle );
		
		closeButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				dispose();
			}
		});
		
		this.getRootPane().setDefaultButton( closeButton );
		closePanel.add( closeButton, c ); 
		return closePanel;
	}
	/**
	 * Render cell of shortcuts table.
	 * @param font
	 * @param alignment
	 * @return DefaultTableCellRenderer
	 */
	private DefaultTableCellRenderer buildTableCellRenderer( final Font font, final int alignment ) {
		return new DefaultTableCellRenderer() {
		    private static final long serialVersionUID = 7727862870364112008L;

			@Override
		    public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
		        super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
		        setFont( font );
		        setHorizontalAlignment( alignment );
		        return this;
		    }	    
		};
	}
	/**
	 * Build path to all shortcut keys from static keys array.
	 * @param shortcut
	 * @param specificKey
	 * @return shortcut from property
	 */
	private String buildShortcutStringProperty( String shortcut, String specificKey ) {
		return new StringBuilder( shortcut ).append( specificKey ).toString();
	}
	/**
	 * Build data injected to shortcuts table based on shortcut table model.
	 * @param tableModel
	 */
	private void buildDataOfShortcutsTable( ShortcutsTableModel tableModel ) {
		final ResourceBundle resource = ResourceBundle.getBundle( KeyAssistConstant.SHORTCUT_KEYS_STRINGS, Locale.getDefault() );
		for( String shortcut : SHORTCUT_KEYS ) {
			tableModel.addEntry( new String[] { 
					resource.getString( buildShortcutStringProperty( shortcut, ".key" ) ).replaceAll( " ", " + " ),
					resource.getString( buildShortcutStringProperty( shortcut, ".description" ) )
			});
		}
	}
	/**
	 * Build columns used in shortcut table in key assist panel.
	 * @param table
	 * @param column
	 * @param preferedWidth
	 * @param render
	 */
	private void buildColumnsOfShortcutTable( JTable table, int column, int preferedWidth, TableCellRenderer render ) {
		table.getColumnModel().getColumn( column ).setPreferredWidth( preferedWidth );
		table.getColumnModel().getColumn( column ).setCellRenderer( render );
	}
	/**
	 * Build full shortcut panel in help menu.
	 * @return shortcuts panel
	 */
	private JPanel keyAssistBuildPanel() {
		if( this.shortcutKeysPanel == null ) {
			ShortcutsTableModel shortcutsTableModel = new ShortcutsTableModel( new String[] { shortcutColumn, descriptionColumn } );			
			JTable keyAssistTable = new JTable( shortcutsTableModel ) {
				private static final long serialVersionUID = -2794749477619913100L;
				/**
				 * Set background gray every second row.
				 */
				@Override
				public Component prepareRenderer( TableCellRenderer renderer, int row, int column ) {
					Component c = super.prepareRenderer( renderer, row, column );
					c.setBackground( row % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY );
					return c;
				}
			};		
			DefaultTableCellRenderer keysColumn = buildTableCellRenderer( new Font( "Serif", Font.BOLD, 14 ), JLabel.RIGHT );
			DefaultTableCellRenderer aboutColumn = buildTableCellRenderer( new Font( "Dialog", Font.ITALIC, 14 ), JLabel.LEFT );
			
			buildColumnsOfShortcutTable( keyAssistTable, 0, 80, keysColumn );
			buildColumnsOfShortcutTable( keyAssistTable, 1, 280, aboutColumn );	
			buildDataOfShortcutsTable( shortcutsTableModel );
			
			JTableHeader headerColumns = keyAssistTable.getTableHeader();
			headerColumns.setFont( new Font( "Serif", Font.BOLD, 14 ) );		
			
			JScrollPane scrollPanel = new JScrollPane();
			scrollPanel.getViewport().add( keyAssistTable );
			
			this.shortcutKeysPanel = new JPanel( new BorderLayout( ));
			this.shortcutKeysPanel.add( scrollPanel );
		}
		return this.shortcutKeysPanel;
	}	
	/**
	 * Set location to show key assist on central view.
	 * @param parent
	 */
	private void setLocation( JFrame parent ) {
		Point point = parent.getLocationOnScreen();
		
		int x = ( int )point.getX() + parent.getWidth() / 2;
		int y = ( int )point.getY() + parent.getHeight() / 2;
		this.setLocation( x - getWidth() / 2, y - getHeight() / 2 );
	}	
	/**
	 * Creates string table model for shortcut keys table.
	 */
	private class ShortcutsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -3706327047378376056L;
		private String[] columnNames;
		private List< String[] > data = new ArrayList< String[] >();
		
		public ShortcutsTableModel( String[] columnNames ) {
			this.columnNames = columnNames;
		}

		public void addEntry( String[] entry ) {
			data.add( entry );
			fireTableRowsInserted( data.size() - 1, data.size() - 1 );
		}
		public int getColumnCount() {
			return columnNames.length;
		}
		public int getRowCount() {
			return data.size();
		}
		public String getColumnName( int col ) {
			return columnNames[ col ];
		}
		public Class< ? > getColumnClass( int c ) {
			return String.class;
		}
		public Object getValueAt( int row, int col ) {
			String[] entry = ( String[] )data.get( row );
			return entry[ col ];
		}
		public boolean isCellEditable( int row, int col ) {
			return false;
		}
	}
}