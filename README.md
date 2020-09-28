<p align="center">
<img src="https://github.com/nestoleh/BottomSheet-Spinner/blob/master/static/icon.png" width="300" height="300" />
</p>

# BottomSheet Spinner [![](https://jitpack.io/v/nestoleh/BottomSheet-Spinner.svg)](https://jitpack.io/#nestoleh/BottomSheet-Spinner)
Spinner with bottom sheet dialog for Android
<p align="center">
<img src="https://github.com/nestoleh/BottomSheet-Spinner/blob/master/static/screenshot-1.jpg" width="270" height="585" />&nbsp;&nbsp;
<img src="https://github.com/nestoleh/BottomSheet-Spinner/blob/master/static/screenshot-2.jpg" width="270" height="585" />&nbsp;&nbsp;
<img src="https://github.com/nestoleh/BottomSheet-Spinner/blob/master/static/screenrecord-1.gif" width="270" height="585" />
</p>


## What is this?
This is a small library for spinner view with displaying options in bottom sheet dialog. This view doesn't improve or extend the default android Spinner. The library doesn't support any other types of showing menu, only bottom sheet dialog.
<br><br>
Library based on BottomSheetDialogFragment and RecyclerView

## Requirements
- Material Components
- AndroidX
- Min SDK 21+

## Install
Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add this line to your app's dependencies:
```
implementation 'com.github.nestoleh:bottomsheet-spinner:version'
```

## Using

1. Add view to your layout file
```
<com.nestoleh.bottomsheetspinner.BottomSheetSpinner
    android:id="@+id/spinner"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```
2. Create an adapter and view holder for spinner items. You can use common **BottomSheetSpinnerAdapter** as base class or list-based version **SimpleBottomSheetSpinnerAdapter**. View holder class has to extend **BottomSheetSpinnerItemViewHolder**. You can use different view layouts and view holders for selected view and dropdown view. 

3. Add your adapter to BottomSheetSpinner
```
val spinnerAdapter = MySpinnerAdapter(listOf("One", "Two", "Three"))
spinnerView.setAdapter(spinnerAdapter)
```
4. Enjoy! 🎉

**P.S.** Also you can see sample of using in <a href="https://github.com/nestoleh/BottomSheet-Spinner/tree/master/sample">sample-application</a>

## Styling

### View styling
BottomSheetView extends FrameLayout, so you can use all styling options supported by FrameLayout. The selected view will be placed inside BottomSheetView as a child view, so don't use theme options that can change child view displaying if you don't want it.

### Bottom sheet dialog styling
BottomSheetView supports the next custom parameters for dialog style^
- **bss_dialogTitle** - used to set dialog title, title doesn't show if it empty or null.
- **bss_dialogTheme** - used to set the theme of bottom sheet dialog and to set styles for dialog inner elements. You can set *bss_dialogRecyclerViewStyle* and *bss_dialogTitleStyle* as parameters of this theme.

### Sample:

**layout.xml**
```
<com.nestoleh.bottomsheetspinner.BottomSheetSpinner
    android:id="@+id/spinner"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@drawable/bg_spinner"
    android:paddingEnd="25dp"
    app:bss_dialogTheme="@style/MyBssDialog.Theme"
    app:bss_dialogTitle="Please select one item"/>

```
**styles.xml**
```
<style name="MyBssDialog.Theme" parent="BottomSheetSpinner.DialogTheme">
    <item name="android:windowIsFloating">false</item>
    <item name="android:windowSoftInputMode">adjustResize</item>
    <item name="bottomSheetStyle">@style/MyBssDialog.BottomSheetStyle</item>
    <item name="android:statusBarColor">@android:color/transparent</item>
    <item name="bss_dialogRecyclerViewStyle">@style/MyBssDialog.RecyclerViewStyle</item>
    <item name="bss_dialogTitleStyle">@style/MyBssDialog.TitleStyle</item>
</style>

<style name="MyBssDialog.BottomSheetStyle" parent="Widget.Design.BottomSheet.Modal">
    <item name="android:background">@drawable/bg_bottom_sheet_dialog</item>
</style>

<style name="MyBssDialog.RecyclerViewStyle">
    <item name="android:layout_marginTop">5dp</item>
</style>

<style name="MyBssDialog.TitleStyle" parent="BottomSheetSpinner.TitleStyle">
    <item name="android:layout_marginTop">24dp</item>
    <item name="android:textAlignment">center</item>
</style>
```

## License

    Copyright 2020 Oleg Nestyuk

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
