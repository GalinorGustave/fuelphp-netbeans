/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2013 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2013 Sun Microsystems, Inc.
 */
package org.netbeans.modules.php.fuel.ui.actions.gotos.statuses;

import org.netbeans.modules.php.api.phpmodule.PhpModule;
import org.netbeans.modules.php.fuel.modules.FuelPhpModule;
import org.netbeans.modules.php.fuel.modules.FuelPhpModule.FILE_TYPE;
import org.openide.filesystems.FileObject;

/**
 *
 * @author junichi11
 */
public final class FuelPhpGoToStatusFactory {

    private FileObject targetFile;
    private int offset;
    private FuelPhpGoToStatus status;
    private static final FuelPhpGoToStatusFactory INSTANCE = new FuelPhpGoToStatusFactory();

    private FuelPhpGoToStatusFactory() {
    }

    public static FuelPhpGoToStatusFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Create object for each file type.
     *
     * @param targetFile
     * @param offset
     * @return
     */
    public FuelPhpGoToStatus create(FileObject targetFile, int offset) {
        if (this.targetFile == targetFile && this.offset == offset) {
            if (status != null) {
                return status;
            }
        }
        PhpModule phpModule = PhpModule.forFileObject(targetFile);
        this.targetFile = targetFile;
        this.offset = offset;

        // get file type
        FuelPhpModule fuelModule = FuelPhpModule.forPhpModule(phpModule);
        FILE_TYPE fileType = fuelModule.getFileType(targetFile);

        switch (fileType) {
            case CONTROLLER:
                status = FuelPhpControllerGoToStatus.getInstance();
                break;
            case MODEL:
                status = FuelPhpModelGoToStatus.getInstance();
                break;
            case VIEW:
                status = FuelPhpViewGoToStatus.getInstance();
                break;
            case VIEW_MODEL:
                status = FuelPhpViewModelGoToStatus.getInstance();
                break;
            case TESTS:
                status = FuelPhpTestCaseGoToStatus.getInstance();
                break;
            default:
                status = FuelPhpDefaultGoToStatus.getInstance();
                break;
        }
        status.setCurrentFile(targetFile);
        status.setCurrentOffset(offset);
        return status;
    }
}
