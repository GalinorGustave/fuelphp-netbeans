/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2012 Sun Microsystems, Inc.
 */
package org.netbeans.modules.php.fuel.ui;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author junichi11
 */
public class FuelPhpCustomizerPanel extends JPanel {
    private static final long serialVersionUID = -7256119527757283230L;

    /**
     * Creates new form FuelPhpCustomizerPanel
     */
    public FuelPhpCustomizerPanel() {
        initComponents();
    }

    public JTextField getFuelNameTextField() {
        return fuelNameTextField;
    }

    public void setFuelNameTextField(String fuelName) {
        fuelNameTextField.setText(fuelName);
    }

    public boolean useTestCaseMethod() {
        return useTestCaseMethodCheckBox.isSelected();
    }

    public void setUseTestCaseMethod(boolean use) {
        this.useTestCaseMethodCheckBox.setSelected(use);
    }

    public boolean ignoreMVCNode() {
        return ignoreMVCNodeCheckBox.isSelected();
    }

    public void setIgnoreMVCNode(boolean ignore) {
        this.ignoreMVCNodeCheckBox.setSelected(ignore);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fuelNameLabel = new javax.swing.JLabel();
        fuelNameTextField = new javax.swing.JTextField();
        useTestCaseMethodCheckBox = new javax.swing.JCheckBox();
        ignoreMVCNodeCheckBox = new javax.swing.JCheckBox();

        org.openide.awt.Mnemonics.setLocalizedText(fuelNameLabel, org.openide.util.NbBundle.getMessage(FuelPhpCustomizerPanel.class, "FuelPhpCustomizerPanel.fuelNameLabel.text")); // NOI18N

        fuelNameTextField.setText(org.openide.util.NbBundle.getMessage(FuelPhpCustomizerPanel.class, "FuelPhpCustomizerPanel.fuelNameTextField.text")); // NOI18N
        fuelNameTextField.setToolTipText(org.openide.util.NbBundle.getMessage(FuelPhpCustomizerPanel.class, "FuelPhpCustomizerPanel.fuelNameTextField.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(useTestCaseMethodCheckBox, org.openide.util.NbBundle.getMessage(FuelPhpCustomizerPanel.class, "FuelPhpCustomizerPanel.useTestCaseMethodCheckBox.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(ignoreMVCNodeCheckBox, org.openide.util.NbBundle.getMessage(FuelPhpCustomizerPanel.class, "FuelPhpCustomizerPanel.ignoreMVCNodeCheckBox.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fuelNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fuelNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(useTestCaseMethodCheckBox)
                    .addComponent(ignoreMVCNodeCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fuelNameLabel)
                    .addComponent(fuelNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(useTestCaseMethodCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ignoreMVCNodeCheckBox)
                .addContainerGap(200, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fuelNameLabel;
    private javax.swing.JTextField fuelNameTextField;
    private javax.swing.JCheckBox ignoreMVCNodeCheckBox;
    private javax.swing.JCheckBox useTestCaseMethodCheckBox;
    // End of variables declaration//GEN-END:variables
}