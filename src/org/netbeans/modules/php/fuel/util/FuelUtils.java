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
package org.netbeans.modules.php.fuel.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.netbeans.modules.php.api.editor.EditorSupport;
import org.netbeans.modules.php.api.editor.PhpClass;
import org.netbeans.modules.php.api.phpmodule.PhpModule;
import org.netbeans.modules.php.fuel.FuelPhpFrameworkProvider;
import org.netbeans.modules.php.fuel.preferences.FuelPhpPreferences;
import org.openide.awt.NotificationDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;

/**
 *
 * @author junichi11
 */
public final class FuelUtils {

    private static final String ACTION_PREFIX = "action_"; // NOI18N
    private static final String CONTROLLER_PREFIX = "Controller_"; // NOI18N
    private static final int DEFAULT_OFFSET = 0;
    private static final String EXT_PHP = "php"; // NOI18N
    private static final String FUEL_APP_CLASSES_CONTROLLER_DIR = "%s/app/classes/controller"; // NOI18N
    private static final String FUEL_APP_CLASSES_MODEL_DIR = "%s/app/classes/model"; // NOI18N
    private static final String FUEL_APP_CLASSES_VIEW_DIR = "%s/app/classes/view";
    private static final String FUEL_APP_VIEWS_DIR = "%s/app/views"; // NOI18N
    private static final String FUEL_APP_MODULES_DIR = "%s/app/modules"; // NOI18N
    private static final String FUEL_AUTOCOMPLETION_PHP = "org-netbeans-modules-php-fuel/fuel_autocompletion.php"; // NOI18N
    private static final String FUEL_AUTOCOMPLETION_TESTCASE_TXT = "org-netbeans-modules-php-fuel/fuel_autocompletion_testcase.txt"; // NOI18N
    private static final String NBPROJECT_DIR_NAME = "nbproject"; // NOI18N
    private static final String UTF8 = "UTF-8"; // NOI18N
    private static final String CLASS_REGEX = "^(class |abstract class )((.+) extends .+|(.+) implements .+|(.+))$";
    private static final String FUEL_AUTOCOMPLETION = "fuel_autocompletion";
    private static final String PHP_EXT = "php";
    private static final String FUEL_AUTOCOMPLETION_WITH_EXT = FUEL_AUTOCOMPLETION + "." + PHP_EXT;
    private static final String FUEL_ICON_16 = "org/netbeans/modules/php/fuel/resources/fuel_icon_16.png";
    private static final String SUCCESS_MSG = "Complete success : " + NBPROJECT_DIR_NAME + "/" + FUEL_AUTOCOMPLETION_WITH_EXT;
    private static final String NOTIFY_TITLE = "Create auto completion file";
    private static final String FAIL_MSG = "Fail : Not Found fuel/core";

    public static JSONArray getJsonArray(URL url) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), UTF8)); // NOI18N
            StringBuilder contents = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                contents.append(str);
            }

            JSONArray jsonArray = new JSONArray(contents.toString());
            return jsonArray;
        } catch (JSONException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    public static void getAutoCompletionFile(FileObject projectDirectory) {
        if (projectDirectory == null) {
            return;
        }
        FileObject nbprojectDirectory = projectDirectory.getFileObject(NBPROJECT_DIR_NAME);
        FileObject autoCompletionFile = FileUtil.getConfigFile(FUEL_AUTOCOMPLETION_PHP);

        if (nbprojectDirectory.getFileObject(autoCompletionFile.getNameExt()) != null) {
            // already exists
            return;
        }

        if (nbprojectDirectory != null && autoCompletionFile != null) {
            try {
                autoCompletionFile.copy(nbprojectDirectory, autoCompletionFile.getName(), autoCompletionFile.getExt());
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    public static void createAutoCompletionFile(PhpModule phpModule, boolean useTestCaseMethod) throws Exception {
        FileObject coreDirectory = getCoreDirectory(phpModule);
        FileObject nbprojectDirectory = phpModule.getProjectDirectory().getFileObject(NBPROJECT_DIR_NAME);
        if (nbprojectDirectory == null) {
            // notification
            NotificationDisplayer.getDefault().notify(NOTIFY_TITLE, ImageUtilities.loadImageIcon(FUEL_ICON_16, true), "Fail", null);
            return;
        }
        FileObject completionFile = nbprojectDirectory.getFileObject(FUEL_AUTOCOMPLETION_WITH_EXT);
        if (completionFile == null) {
            completionFile = nbprojectDirectory.createData(FUEL_AUTOCOMPLETION, PHP_EXT);
        }
        if (coreDirectory != null) {
            FileObject classesDirectory = coreDirectory.getFileObject("classes"); // NOI18N
            FileObject[] children = classesDirectory.getChildren();
            Arrays.sort(children, new FileObjectComparator());

            PrintWriter printWriter = new PrintWriter(completionFile.getOutputStream());
            printWriter.println("<?php"); // NOI18N
            try {
                writeAutoCompletionFile(printWriter, children, useTestCaseMethod);
            } finally {
                printWriter.close();
            }
        } else {
            NotificationDisplayer.getDefault().notify(NOTIFY_TITLE, ImageUtilities.loadImageIcon(FUEL_ICON_16, true), FAIL_MSG, null);
            return;
        }
        NotificationDisplayer.getDefault().notify(NOTIFY_TITLE, ImageUtilities.loadImageIcon(FUEL_ICON_16, true), SUCCESS_MSG, null);
    }

    /**
     * Write auto completion file
     *
     * @param printWriter
     * @param inputDirectories
     * @throws IOException
     */
    private static void writeAutoCompletionFile(PrintWriter printWriter, FileObject[] inputDirectories, boolean useTestMethod) throws IOException {
        if (inputDirectories == null) {
            return;
        }
        for (FileObject input : inputDirectories) {
            if (input.isFolder()) {
                FileObject[] children = input.getChildren();
                Arrays.sort(children, new FileObjectComparator());
                writeAutoCompletionFile(printWriter, input.getChildren(), useTestMethod);
            } else {
                List<String> lines = input.asLines();
                for (String line : lines) {
                    if (line.startsWith("interface")) { // NOI18N
                        break;
                    }
                    String writeString = "";
                    Pattern pattern = Pattern.compile(CLASS_REGEX);
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String className = "";
                        for (int i = 0; i < 3; i++) {
                            String match = matcher.group(3 + i);
                            if (match != null && !match.isEmpty()) {
                                className = matcher.group(3 + i);
                            }
                        }
                        writeString = matcher.group(1) + className + " extends Fuel\\Core\\" + className + " {}";
                        if (useTestMethod && writeString.contains("TestCase")) { // NOI18N
                            writeTestCaseMethod(printWriter);
                        }
                        printWriter.println(writeString);
                    }
                }
            }
        }
    }

    /**
     * Write TestCase method. This is option.
     *
     * @param printWriter
     */
    private static void writeTestCaseMethod(PrintWriter printWriter) throws IOException {
        FileObject testCaseCompletionFile = FileUtil.getConfigFile(FUEL_AUTOCOMPLETION_TESTCASE_TXT);
        for (String line : testCaseCompletionFile.asLines()) {
            printWriter.println(line);
        }
    }

    /**
     * Get fuel directory
     *
     * @param phpModule
     * @return
     */
    public static FileObject getFuelDirectory(PhpModule phpModule) {
        String fuelName = FuelPhpPreferences.getFuelName(phpModule);
        return phpModule.getSourceDirectory().getFileObject(fuelName);
    }

    /**
     * get fuel core directory
     *
     * @param phpModule
     * @return
     */
    public static FileObject getCoreDirectory(PhpModule phpModule) {
        String fuelName = FuelPhpPreferences.getFuelName(phpModule);
        return phpModule.getSourceDirectory().getFileObject(fuelName + "/core"); // NOI18N
    }

    /**
     * Check controller file
     *
     * @param controller controller FileObject
     * @return if prefix is "Controller_", return true.
     */
    public static boolean isController(FileObject controller) {
        if (controller == null) {
            return false;
        }
        EditorSupport editorSupport = Lookup.getDefault().lookup(EditorSupport.class);
        Collection<PhpClass> phpClasses = editorSupport.getClasses(controller);
        if (phpClasses.size() != 1) {
            return false;
        }

        PhpClass phpClass = null;
        for (PhpClass pc : phpClasses) {
            phpClass = pc;
        }

        if (phpClass == null) {
            return false;
        }

        return phpClass.getName().startsWith(CONTROLLER_PREFIX);
    }

    /**
     * Check view file
     *
     * @param view view FileObject
     * @return if the view file is in views directory, return true.
     */
    public static boolean isView(FileObject view) {
        if (view == null || !EXT_PHP.equals(view.getExt())) {
            return false;
        }
        FileObject viewsDirectory = getViewsDirectory(view);

        String viewsDirectoryPath = viewsDirectory.getPath();
        String viewPath = view.getPath();

        return viewPath.startsWith(viewsDirectoryPath);
    }

    /**
     * Check view model file
     *
     * @param viewModel view model FileObject
     * @return if the view model file is in view model directory, return true.
     */
    public static boolean isViewModel(FileObject viewModel) {
        if (viewModel == null || !EXT_PHP.equals(viewModel.getExt())) {
            return false;
        }
        FileObject viewModelDirectory = getViewModelDirectory(viewModel);

        String viewModelDirectoryPath = viewModelDirectory.getPath();
        String viewModelPath = viewModel.getPath();

        return viewModelPath.startsWith(viewModelDirectoryPath);
    }

    /**
     * Get views directory (fuel/app/views)
     *
     * @param fo FileObject
     * @return views directory FileObject
     */
    public static FileObject getViewsDirectory(FileObject fo) {
        PhpModule pm = PhpModule.forFileObject(fo);
        return getViewsDirectory(pm);
    }

    /**
     * Get views directory (fuel/app/views)
     *
     * @param phpModule PhpModule
     * @return views directory FileObject
     */
    public static FileObject getViewsDirectory(PhpModule phpModule) {
        return getDirectory(phpModule, FUEL_APP_VIEWS_DIR);
    }

    /**
     * Get controller directory (fuel/app/classes/controller)
     *
     * @param fo FileObject
     * @return controller directory FileObject
     */
    public static FileObject getControllerDirectory(FileObject fo) {
        PhpModule pm = PhpModule.forFileObject(fo);
        return getControllerDirectory(pm);
    }

    /**
     * Get controller directory (fuel/app/classes/controller)
     *
     * @param phpModule FileObject
     * @return controller directory FileObject
     */
    public static FileObject getControllerDirectory(PhpModule phpModule) {
        return getDirectory(phpModule, FUEL_APP_CLASSES_CONTROLLER_DIR);
    }

    /**
     * Get model directory (fuel/app/classes/model)
     *
     * @param phpModule FileObject
     * @return model directory FileObject
     */
    public static FileObject getModelDirectory(PhpModule phpModule) {
        return getDirectory(phpModule, FUEL_APP_CLASSES_MODEL_DIR);
    }

    /**
     * Get view model directory (fuel/app/classes/view)
     *
     * @param fo FileObject
     * @return view model directory FileObject
     */
    public static FileObject getViewModelDirectory(FileObject fo) {
        PhpModule pm = PhpModule.forFileObject(fo);
        return getViewModelDirectory(pm);
    }

    /**
     * Get view model directory (fuel/app/classes/view)
     *
     * @param phpMoudle PhpModule
     * @return view model directory FileObject
     */
    public static FileObject getViewModelDirectory(PhpModule phpModule) {
        return getDirectory(phpModule, FUEL_APP_CLASSES_VIEW_DIR);
    }

    /**
     * Get modules directory (fuel/app/modules)
     *
     * @param phpMoudle PhpModule
     * @return modules directory FileObject
     */
    public static FileObject getModulesDirectory(PhpModule phpModule) {
        return getDirectory(phpModule, FUEL_APP_MODULES_DIR);
    }

    /**
     * Get directory
     *
     * @param phpModule
     * @param directoryPath
     * @return
     */
    public static FileObject getDirectory(PhpModule phpModule, String directoryPath) {
        if (phpModule == null) {
            return null;
        }
        String path = String.format(directoryPath, FuelPhpPreferences.getFuelName(phpModule));
        return phpModule.getSourceDirectory().getFileObject(path);
    }

    /**
     * Check action name
     *
     * @param name action name
     * @return name starts with "action_", then return true.
     */
    public static boolean isActionName(String name) {
        return name.startsWith(ACTION_PREFIX) && !name.equals(ACTION_PREFIX);
    }

    /**
     * Get infered controller
     *
     * @param view view file
     * @return controller FileObject
     */
    public static FileObject getInferedController(FileObject view) {
        if (!isView(view) && !isViewModel(view)) {
            return null;
        }
        // view file path from views directory
        String viewPath = getViewPath(view);
        String controllerPath = viewPath.replace("/" + view.getNameExt(), "") + ".php"; // NOI18N

        // get controller
        FileObject controller = getControllerDirectory(view).getFileObject(controllerPath);
        if (!isController(controller)) {
            return null;
        }
        return controller;
    }

    /**
     * Get view file path from views / view model directory
     *
     * @param view
     * @return view file relative path. contain extension.
     */
    private static String getViewPath(FileObject view) {
        String viewDirectoryPath = null;
        String viewPath = view.getPath();
        if (isView(view)) {
            viewDirectoryPath = getViewsDirectory(view).getPath();
        } else if (isViewModel(view)) {
            viewDirectoryPath = getViewModelDirectory(view).getPath();
        } else {
            return null;
        }
        String replacePath = viewPath.replace(viewDirectoryPath, ""); // NOI18N
        if (replacePath.startsWith("/")) { // NOI18N
            replacePath = replacePath.replaceFirst("/", ""); // NOI18N
        }
        return replacePath;
    }

    /**
     * Get infered controller action name
     *
     * @param view view FileObject
     * @return action name.
     */
    public static String getInferedActionName(FileObject view) {
        if (!isView(view) && !isViewModel(view)) {
            return null;
        }
        return ACTION_PREFIX + view.getName();
    }

    /**
     * Check whether FuelPHP
     *
     * @param phpMoudle
     * @return true if in fuelphp module, otherwise false.
     */
    public static boolean isFuelPHP(PhpModule phpMoudle) {
        return FuelPhpFrameworkProvider.getInstance().isInPhpModule(phpMoudle);
    }

    private static class FileObjectComparator implements Comparator<FileObject> {

        @Override
        public int compare(FileObject o1, FileObject o2) {
            if (o1.isFolder() && o2.isData()) {
                return -1;
            }
            if (o1.isData() && o2.isFolder()) {
                return 1;
            }
            if ((o1.isFolder() && o2.isFolder())
                    || (o1.isData() && o2.isData())) {
                List<String> list = new ArrayList<String>();
                String name1 = o1.getName();
                String name2 = o2.getName();

                list.add(name1);
                list.add(name2);
                Collections.sort(list);
                if (name1.equals(list.get(0))) {
                    return -1;
                } else {
                    return 1;
                }
            }
            return 0;
        }
    }
}
