
package com.example.mcstats;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppFrame::new);
    }
}

class AppFrame extends JFrame {

    private final DefaultTableModel model;
    private final JTable table;
    private final JTextField searchField;
    private final StatsService service;
    private final TableRowSorter<DefaultTableModel> sorter;

    public AppFrame() {
        super("Minecraft Stats Viewer");

        service = new StatsService();

        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"Player", "UUID", "Kills", "Deaths", "Playtime"}, 0);

        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        searchField = new JTextField();

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        JButton loadButton = new JButton("Load Sample Data");
        loadButton.addActionListener(e -> loadSample());

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Search: "), BorderLayout.WEST);
        top.add(searchField, BorderLayout.CENTER);
        top.add(loadButton, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadSample() {
        model.setRowCount(0);

        List<StatsRecord> records = List.of(
                new StatsRecord("00000000000000000000000000000000", 5, 2, 120),
                new StatsRecord("069a79f444e94726a5befca90e38aaf5", 20, 10, 500)
        );

        for (StatsRecord r : records) {
            String name = service.resolveName(r.uuid);
            model.addRow(new Object[]{name, r.uuid, r.kills, r.deaths, r.playtime});
        }
    }

    private void filter() {
        String text = searchField.getText();
        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }
}
