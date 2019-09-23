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
 *       This class contains contributions from the the original
 *       RCP version of the class (org.eclipse.swt.dnd.Clipboard),
 *       contributed by IBM Corporation, which source is available 
 *       under http://www.eclipse.org.  
 **********************************************************************/

package org.eclipse.swt.dnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.internal.ClipboardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * RAP implementation of the SWT Clipboard class.
 * <p>
 * Note : this class doesn't handle correctly concurrent Clipboard
 * modifications.
 * 
 * @author David Marina
 * 
 */
public class Clipboard
{
    /**
     * Container for the map associating transfers to objects
     */
    private class TransferDataContainer
    {
        /** The map associating transfers to objects */
        private final Map<Transfer, Object> map;


        /**
         * Creates a new TransferDataContainer
         */
        public TransferDataContainer()
        {
            this.map = new HashMap<Transfer, Object>();
        }

        /**
         * @return the map associating transfers to objects
         */
        public Map<Transfer, Object> getMap()
        {
            return this.map;
        }
    }


    /**
     * the instance of the container of the map associating transfers to objects
     */
    private final TransferDataContainer transferDataContainer;

    private Display display;

    /**
     * Creates a new Clipboard
     * 
     * @param display
     *            the display of this clipboard
     */
    public Clipboard(Display display)
    {   
        Display disp = display;
        if (display == null) {
            disp = Display.getCurrent();
            if (disp == null) {
                disp = Display.getDefault();
            }
        }
        if (disp.getThread() != Thread.currentThread()) {
            DND.error(SWT.ERROR_THREAD_INVALID_ACCESS);
        }
        Object clipboardContentObject = RWT.getUISession(disp).getHttpSession().getAttribute("Clipboard");
        if (clipboardContentObject != null && clipboardContentObject instanceof TransferDataContainer)
        {
            this.transferDataContainer = (TransferDataContainer) clipboardContentObject;
        }
        else
        {
            this.transferDataContainer = new TransferDataContainer();
            RWT.getUISession(disp).getHttpSession().setAttribute("Clipboard", this.transferDataContainer);
        }
        this.display = disp;
    }

    /**
     * Disposes the {@link Clipboard} object.
     */
    public void dispose()
    {
        if (isDisposed()) return;
        if (this.display.getThread() != Thread.currentThread()) DND.error(SWT.ERROR_THREAD_INVALID_ACCESS);
        this.display = null;
    }
    
    /**
     * Returns <code>true</code> if the clipboard has been disposed,
     * and <code>false</code> otherwise.
     * <p>
     * This method gets the dispose state for the clipboard.
     * When a clipboard has been disposed, it is an error to
     * invoke any other method using the clipboard.
     * </p>
     *
     * @return <code>true</code> when the widget is disposed and <code>false</code> otherwise
     * 
     * @since 3.0
     */
    public boolean isDisposed () {
        return (display == null);
    }

    /**
     * If the {@link Transfer} array contains a {@link TextTransfer} object, it
     * is created a new dialog containing the text. This text is automatically
     * selected so the user can copy it easily from the dialog to the client
     * Clipboard.
     * 
     * @param objects
     *            the array of objects to copy to the Clipboard
     * @param transfers
     *            the array defining the type of the objects to be copied to the
     *            Clipboard
     */
    public void setContents(Object[] objects, Transfer[] transfers)
    {
        checkWidget();
        String clipboardText = "";
        boolean isOnlyTextTransfer = true;
        String title = "";
        String message = "";

        for (int i = 0; i < transfers.length; i++)
        {
            this.transferDataContainer.getMap().put(transfers[i], objects[i]);
            if (transfers[i] instanceof TextTransfer)
            {

                clipboardText = (String) objects[i];
                if (isOnlyTextTransfer)
                {
                    title = ClipboardDialog.DEFAULT_DIALOG_TITLE;
                    message = ClipboardDialog.DEFAULT_DIALOG_MESSAGE;
                }
            }
            else
            {
                isOnlyTextTransfer = false;
                title = "Copy " + transfers[i].toString();
                message = "The " + transfers[i].toString() + " has been copied.\n\n"
                          + ClipboardDialog.DEFAULT_DIALOG_MESSAGE;
            }

        }

        ClipboardDialog dialog = new ClipboardDialog(new Shell(), title, message);
        dialog.setClipboardText(clipboardText);
        dialog.open();
    }

    /**
     * Get the content of the clipboard
     * 
     * @param instance
     *            The type of transfer instance
     * @return The content associated to this transfer instance
     */
    public Object getContents(Transfer instance)
    {
        checkWidget();
        return this.transferDataContainer.getMap().get(instance);
    }

    /**
     * @return
     */
    public TransferData[] getAvailableTypes()
    {
        checkWidget();
        List<TransferData> retList = new ArrayList<TransferData>();
        Set<Transfer> transfers = this.transferDataContainer.getMap().keySet();
        Iterator<Transfer> it = transfers.iterator();
        while (it.hasNext())
        {
            Transfer tr = it.next();
            for (TransferData td : tr.getSupportedTypes())
            {
                if (!retList.contains(td))
                {
                    retList.add(td);
                }
            }
        }
        return retList.toArray(new TransferData[0]);
    }
    
    /**
     * Throws an <code>SWTException</code> if the receiver can not
     * be accessed by the caller. This may include both checks on
     * the state of the receiver and more generally on the entire
     * execution context. This method <em>should</em> be called by
     * widget implementors to enforce the standard SWT invariants.
     * <p>
     * Currently, it is an error to invoke any method (other than
     * <code>isDisposed()</code>) on a widget that has had its 
     * <code>dispose()</code> method called. It is also an error
     * to call widget methods from any thread that is different
     * from the thread that created the widget.
     * </p><p>
     * In future releases of SWT, there may be more or fewer error
     * checks and exceptions may be thrown for different reasons.
     * </p>
     *
     * @exception SWTException <ul>
     *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
     *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
     * </ul>
     */
    protected void checkWidget () {
        if (this.display == null) DND.error (SWT.ERROR_WIDGET_DISPOSED);
        if (this.display.getThread() != Thread.currentThread ()) DND.error (SWT.ERROR_THREAD_INVALID_ACCESS);
        if (this.display.isDisposed()) DND.error(SWT.ERROR_WIDGET_DISPOSED);
    }

}

// -----------------------------------------------------------------------------
