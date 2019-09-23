/*********************************************************************
 * Copyright (c) 2019 European Space Agency
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     European Space Agency - initial API and implementation
 **********************************************************************/

package org.eclipse.swt.widgets;

/**
 * FileDialog implementation for RAP, extending the one provided by the
 * <i>org.eclipse.rap.filedialog</i> plugin.
 * 
 * @author Jean Schuetz
 * 
 */
public class FileDialogRAP extends FileDialog
{

    /** The serialVersionUID of this FileDialogRAP.java */
    private static final long serialVersionUID = 4271114673052111996L;

    /** The filterPath of this FileDialog */
    private String filterPath;


    /**
     * Creates a new FileDialog
     * 
     * @param parentShell
     * @param style
     */
    public FileDialogRAP(Shell parentShell, int style)
    {
        super(parentShell, style);
    }

    /**
     * Creates a new FileDialog
     * 
     * @param parentShell
     */
    public FileDialogRAP(Shell parentShell)
    {
        super(parentShell);
    }

    /**
     * @param filterNames
     */
    public void setFilterNames(String[] filterNames)
    {
        // Do nothing
    }

    /**
     * @param filterExtensions
     */
    public void setFilterExtensions(String[] filterExtensions)
    {
        // Do nothing
    }

    /**
     * (Pre-)sets the filename
     * 
     * @param fileName
     */
    public void setFileName(String fileName)
    {
        // Do nothing
    }

    /**
     * Sets the path to filter on
     * 
     * @param lastOpenedConfigDirectory
     */
    public void setFilterPath(String lastOpenedConfigDirectory)
    {
        this.filterPath = lastOpenedConfigDirectory;
    }

    /**
     * Returns the directory path that the dialog will use, or an empty string
     * if this is not set. File names in this path will appear in the dialog,
     * filtered according to the filter extensions.
     * 
     * @return a String containing the filterPath of this FileDialog
     */
    public String getFilterPath()
    {
        return this.filterPath != null ? this.filterPath : "";
    }

}

// -----------------------------------------------------------------------------
