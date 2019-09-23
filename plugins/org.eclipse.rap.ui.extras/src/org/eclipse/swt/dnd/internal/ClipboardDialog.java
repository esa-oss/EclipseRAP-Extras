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

package org.eclipse.swt.dnd.internal;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author David Marina
 * @since 2.6.4
 */
public class ClipboardDialog extends TitleAreaDialog
{
    /** The dialog title */
    public static final String DEFAULT_DIALOG_TITLE = "Copy Text to Clipboard";

    /** The dialog title */
    public static final String DEFAULT_DIALOG_MESSAGE = "Select the text contained in this dialog and press Ctrl+c for copying it into the Clipboard.";

    /** The serialVersionUID of this ClipboardDialog.java */
    private static final long serialVersionUID = -3793647127220668878L;

    /** The title */
    private static final String TITLE = "Copy to Clipboard:";

    /** The name of the section in the dialog settings */
    private static final String DIALOG_SETTINGS_SECTION_NAME = "Workbench";

    /** Label to identify the dialog settings in the settings store */
    private static final String DIALOG_SETTINGS = "DIALOG_SETTINGS";

    /** The dialog id to identify the dialog settings in the settings store */
    private static final String DIALOG_ID = "org.eclipse.swt.dnd.internal.ClipboardDialog";

    /**
     * The attribute that identify the settings of this dialog in the settings
     * store
     */
    private static final String SETTINGS_ATTRIBUTE = DIALOG_ID + DIALOG_SETTINGS;

    /**
     * The dialog title
     */
    private final String title;

    /**
     * The text message of the dialog
     */
    private final String message;

    /** The text to display in this clipboard dialog */
    private String text;

    /** Text control that displays the text in this clipboard dialog */
    private Text textControl;

    /** The reference to the dialog settings */
    private IDialogSettings dialogSettings;


    /**
     * Creates a new ClipboardDialog
     * 
     * @param parentShell
     *            the reference to the parent shell
     */
    public ClipboardDialog(Shell parentShell)
    {
        this(parentShell,
             new DialogSettings(DIALOG_SETTINGS_SECTION_NAME),
             DEFAULT_DIALOG_TITLE,
             DEFAULT_DIALOG_MESSAGE);
    }

    /**
     * Creates a new ClipboardDialog
     * 
     * @param parentShell
     *            the reference to the parent shell
     * @param title
     *            the dialog title
     * @param message
     *            the dialog message
     */
    public ClipboardDialog(Shell parentShell, String title, String message)
    {
        this(parentShell, new DialogSettings(DIALOG_SETTINGS_SECTION_NAME), title, message);
    }

    /**
     * Creates a new ClipboardDialog
     * 
     * @param parentShell
     *            the reference to the parent shell
     * @param settings
     *            the dialog settings
     * @param title
     *            the dialog title
     * @param message
     *            the dialog message
     */
    public ClipboardDialog(Shell parentShell, IDialogSettings settings, String title, String message)
    {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);

        this.title = title;
        this.message = message;

        this.dialogSettings = settings;
        loadDialogSettings();
    }

    /**
     * Loads the dialog settings stored in the settings store (if any).
     */
    private void loadDialogSettings()
    {
        String strSettings = RWT.getSettingStore().getAttribute(SETTINGS_ATTRIBUTE);
        if (strSettings != null)
        {
            try
            {
                StringReader reader = new StringReader(strSettings);
                this.dialogSettings.load(reader);
            }
            catch (IOException e)
            {
                System.err.println("Error while loading dialog settings for dialog " + DIALOG_ID + ": "
                                   + e.getMessage());
            }
        }

    }

    /** {@inheritDoc} */
    @Override
    protected void configureShell(Shell newShell)
    {
        super.configureShell(newShell);
        newShell.setText(this.title);
    }

    /** {@inheritDoc} */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        setTitle(TITLE);
        setMessage(this.message);

        Composite composite = new Composite(parent, SWT.BORDER);
        GridLayout gl = new GridLayout(1, true);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        composite.setLayoutData(gd);
        composite.setLayout(gl);

        this.textControl = new Text(composite, SWT.MULTI);
        GridData gdText = new GridData(SWT.FILL, SWT.FILL, true, true);
        this.textControl.setLayoutData(gdText);
        this.textControl.setText(this.text);

        this.textControl.addFocusListener(new FocusAdapter()
        {
            /** The serialVersionUID of this ClipboardDialog.java */
            private static final long serialVersionUID = 351240186225267461L;


            @Override
            public void focusGained(FocusEvent event)
            {
                ClipboardDialog.this.textControl.selectAll();
            }
        });

        this.textControl.setFocus();
        return composite;
    }

    /**
     * Sets the text to be displayed in this clipboard dialog.
     * 
     * @param text
     *            the text to be displayed in this clipboard dialog
     */
    public void setClipboardText(String text)
    {
        this.text = text;
        if (this.textControl != null)
        {
            this.textControl.setText(text);
            this.textControl.selectAll();
        }
    }

    /** {@inheritDoc} */
    @Override
    protected IDialogSettings getDialogBoundsSettings()
    {
        if (this.dialogSettings == null)
        {
            return null;
        }

        IDialogSettings section = this.dialogSettings.getSection(DIALOG_ID);
        if (section == null)
        {
            section = this.dialogSettings.addNewSection(DIALOG_ID);
        }
        return section;
    }

    /** {@inheritDoc} */
    @Override
    public boolean close()
    {
        boolean ret = super.close();

        // Before closing, save the current settings in the setting store
        StringWriter writer = new StringWriter();
        try
        {
            this.dialogSettings.save(writer);
            RWT.getSettingStore().setAttribute(DIALOG_ID + DIALOG_SETTINGS, writer.toString());
        }
        catch (IOException e)
        {
            System.err.println("Error while saving dialog settings for dialog " + DIALOG_ID + ": " + e.getMessage());
        }
        return ret;
    }

    /** {@inheritDoc} */
    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
        // create only OK button
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.get().OK_LABEL, true);
    }
}

// -----------------------------------------------------------------------------
