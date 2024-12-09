/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mylibrary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

import javax.swing.JTextField;

import java.awt.Color;

import java.awt.Color;

import java.awt.event.FocusAdapter;

import java.awt.event.FocusAdapter;

public class PlaceholderTextField extends JTextField {

    private String placeholder;

    public PlaceholderTextField(String placeholder) {
        this.placeholder = placeholder;
        setForeground(Color.LIGHT_GRAY);
        setText(placeholder);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setForeground(Color.LIGHT_GRAY);
                    setText(placeholder);
                }
            }
        });
    }

    @Override
    public void setText(String t) {
        super.setText(t);
        if (t.isEmpty()) {
            setForeground(Color.LIGHT_GRAY);
            super.setText(placeholder);
        }
    }

    @Override
    public String getText() {
        if (super.getText().equals(placeholder)) {
            return "";
        }
        return super.getText();
    }
}
