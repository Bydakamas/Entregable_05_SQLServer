/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.cejemplo04;

import Vista.JFIngreso;
import javax.swing.UIManager;
import com.jtattoo.plaf.texture.TextureLookAndFeel; // Importamos el TextureLookAndFeel


/**
 *
 * @author USER 17
 */
public class CEjemplo04 {

    public static void main(String[] args) {
        try {
            // Establecer el Look and Feel de Texture desde JTattoo
            UIManager.setLookAndFeel(new TextureLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear una instancia del formulario
        JFIngreso formulario = new JFIngreso();
        // Hacer visible el formulario
        formulario.setVisible(true);
    }
}
