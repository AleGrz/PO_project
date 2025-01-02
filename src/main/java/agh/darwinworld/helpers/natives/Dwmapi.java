package agh.darwinworld.helpers.natives;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.WinDef;

/**
 * Desktop Window Manager
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/api/dwmapi/">Microsoft docs</a>
 */
public interface Dwmapi extends Library {
    Dwmapi INSTANCE = Native.load("dwmapi", Dwmapi.class);

    /**
     * Window's native API.
     *
     * @param hwnd        the handle to the window for which the attribute value is to be set.
     * @param dwAttribute a flag describing which value to set, specified as a value of the
     *                    DWMWINDOWATTRIBUTE enumeration. This parameter specifies which
     *                    attribute to set, and the pvAttribute parameter points to an
     *                    object containing the attribute value.
     * @param pvAttribute a pointer to an object containing the attribute value to set.
     *                    The type of the value set depends on the value of the dwAttribute
     *                    parameter. The DWMWINDOWATTRIBUTE enumeration topic indicates, in
     *                    the row for each flag, what type of value you should pass a pointer
     *                    to in the pvAttribute parameter.
     * @param cbAttribute the size, in bytes, of the attribute value being set via the pvAttribute
     *                    parameter. The type of the value set, and therefore its size in bytes,
     *                    depends on the value of the dwAttribute parameter.
     * @return If the function succeeds, it returns S_OK. Otherwise, it returns an HRESULT error code.
     * @see <a href="https://learn.microsoft.com/en-us/windows/win32/api/dwmapi/nf-dwmapi-dwmsetwindowattribute">Microsoft docs</a>
     */
    int DwmSetWindowAttribute(WinDef.HWND hwnd, int dwAttribute, PointerType pvAttribute, int cbAttribute);
}
