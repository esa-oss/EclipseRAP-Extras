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

package org.eclipse.jface.preference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.swt.widgets.Composite;

/**
 * A specific editor to edit file/directory paths inside Preference Pages.
 * 
 * @author Lucjan Kornacki
 * @since 2.6.4
 */
public class PathEditor extends ListEditor
{
    /** The serialVersionUID of this PathEditor */
    private static final long serialVersionUID = -5285643955433218905L;


    /**
     * Creates a new PathEditor
     */
    protected PathEditor()
    {
        // Default constructor
    }

    /**
     * Creates a new PathEditor.
     * 
     * @param name
     *            the name of the preference this editor is associated with
     * @param labelText
     *            a String containing the text for this editor
     * @param dirSelectorText
     *            a String containing the text for the directory selector
     * @param parent
     *            a reference to the parent Composite on which to create this
     *            PathEditor
     */
    public PathEditor(String name, String labelText, String dirSelectorText, Composite parent)
    {
        init(name, labelText);
        createControl(parent);
        // Make not editable in RAP
        getDownButton().setVisible(false);
        getRemoveButton().setVisible(false);
        getUpButton().setVisible(false);
        getAddButton().setVisible(false);
    }

    /** {@inheritDoc} */
    @Override
    protected String createList(String[] items)
    {
        StringBuffer result = new StringBuffer("");

        for (String s : items)
        {
            result.append(s);
            result.append(File.pathSeparator);
        }

        return result.toString();
    }

    /** {@inheritDoc} */
    @Override
    protected String getNewInputObject()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected String[] parseString(String inputString)
    {
        StringTokenizer tokenizer = new StringTokenizer(inputString, File.pathSeparator + "\n\r");
        List<String> result = new ArrayList<String>();
        while (tokenizer.hasMoreElements())
        {
            result.add(tokenizer.nextToken());
        }
        return result.toArray(new String[result.size()]);
    }
}
