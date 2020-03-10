# BlurTutorial [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="https://www.cleveroad.com/public/comercial/label-android.svg" height="19"> <a href="https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts"><img src="https://www.cleveroad.com/public/comercial/label-cleveroad.svg" height="19"></a>
![Header image](/images/header.jpg)
## Meet BlurTutorial, an Android-based library made by Cleveroad
Hurry to check our newest library that helps to blur the background in Android apps. BlurTutorial highlights the needed UI elements and brings users' attention to the app's parts you're introducing. You can easily apply a blurred view behind the dialog window and context menu, singling out the important elements.

![Demo image](/images/demo.gif)

## Want to play around with BlurTutorial animation? Check Appetize!

We’ve created the library to help users enhance their app's usability and appearance. BlurTutorial handles both the tasks.

[![Awesome](/images/appertize.png)](https://appetize.io/app/y8jzy8hdvwxr6nuucympeqgx5c)

[![Awesome](/images/logo-footer_.png)](https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts)
<br/>

### Setup ###
To use BlurTutorial add dependency to your project:
```groovy
    implementation 'com.cleveroad.blur_tutorial:blur_tutorial:1.0.1'
```


### Usage ###
#### BlurTutorial ####
The main component of library is the <b>BlurTutorial</b> interface.

```kotlin
    /*
     Add list of states to tutorial.
    */
    fun addAllStates(states: List<TutorialState>)

    /*
     Add one state to tutorial.
    */
    fun addState(state: TutorialState)

    /*
     Add one state by position.
    */
    fun setState(state: TutorialState, index: Int)

    /*
     Remove state from tutorial.
    */
    fun removeState(state: TutorialState)

    /*
     Clear all states of tutorial. 
     Cancel tutorial process.
    */
    fun clearStates()

    /*
     Get current state of tutorial.
    */
    fun getCurrentState(): TutorialState?

    /*
     Start tutorial process.
     
     Should be called in [Activity.onResume] or [Fragment.onResume] or later.
     If you also highlight action bar menu items,
     call it in [Activity.onPrepareOptionsMenu] or [Fragment.onPrepareOptionsMenu] or later.
    */
    fun start()

    /*
     Go to next state of tutorial.
     Finish the current state.
    */
    fun next()

    /*
     Interrupt tutorial process.
     It can be renewed by calling next() or start()
    */
    fun interrupt()
    
    /* 
     Change configuration of
     current [BlurTutorial] instance.
    */
    fun configure(): TutorialBuilder

    /* 
     Save state of tutorial. 
     You must call it in onSaveInstanceState()
     of your Fragment or Activity.
    */
    fun onSaveInstanceState(outState: Bundle)

    /* 
     Restore state of tutorial. 
     You must call it in onRestoreInstanceState() of 
     your Fragment or Activity.
    */
    fun onRestoreInstanceState(savedState: Bundle?)

    /*
     Release resources. 
     You must call it in onDestroy() of 
     your Fragment or Activity.
    */
    fun onDestroy()
```
#### TutorialState ####
<b>TutorialState</b> is an interface, that describes UI item to explain. There are a few data classes, that implement this interface: 
* <b>ViewState</b> - describes the View to highlight.
* <b>MenuState</b> - describes the menu item to highlight.
* <b>RecyclerItemState</b> - describes the RecyclerView item to highlight.
* <b>PathState</b> - describes the geometric figure to highlight. 
By using this state you can highlight a part of view or a part of visible screen area.
#### TutorialBuilder ####
To create an instance of <b>BlurTutorial</b> you have to use <b>TutorialBuilder</b>.

|  Builder method name | parameter description | is required  |
|---|---|---|
| withParent  |  Root of your layout. Will be blurred or dimmed.  |  yes  |
| withPopupLayout  |  Layout of popup window.  |  yes  |
| withPopupAppearAnimation  |  Popup appear animation resource  |  no  |
| withPopupDisappearAnimation  |  Popup disappear animation resource  |  no  |
| withPopupAppearAnimator  |  Popup appear animator resource  |  no |
| withPopupDisappearAnimator | Popup disappear animator resource | no |
| withPopupXOffset |  X offset of popup window  |  no  |
|  withPopupYOffset  |  Y offset of popup window  |  no  |
|  withPopupCornerRadius  |  Radius of popup corners  |  no  |
|  withBlurRadius  |  Radius of blur. Must be in a range (0.0; 25.0]. By default, it's 10.0  |  no  |
|  withOverlayColor  |  Color of overlay, that will be displayed above blurred or dimmed background.  |  no  |
|  withHighlightType  |  Type of highlight: Blur or Dim. By default, it's Blur.  |  no  |
|  withListener  |  Listener of tutorial process  |  yes  |

#### TutorialListener ####
Also you have to set listener of tutorial process:

```kotlin
interface TutorialListener {

    /*
     Called before state is entered.
     Use it to prepare UI for view highlighting.
     E.g. smoothly scroll to this view.
    */
    override fun onStateEnter(state: TutorialState)

    // Called after state exit.
    override fun onStateExit(state: TutorialState)

    // Called on state error. E.g. highlighted view is not visible, etc.
    override fun onStateError(state: TutorialState, error: StateException)

    /* 
     Called when popup view is inflated.
     Use this method to populate your popup view.
    */
    override fun onPopupViewInflated(state: TutorialState, popupView: View)

    // Called when tutorial process is finished.
    override fun onFinish()
}
```

#### Fragment/Activity usage ####
```kotlin
companion object {
    private const val VIEW_ID = 0
    private const val MENU_ITEM_ID = 1
    private const val RECYCLER_ITEM_ID = 2
    private const val PATH_ID = 3
    
    private const val RECYCLER_ITEM_INDEX = 10
    ...
}
...

private val path = Path().apply {
        addCircle(500F, 500F, 300F, Path.Direction.CCW) 
}

private val states by lazy {
        listOf(ViewState(VIEW_ID, tvHighlight),
               MenuState(MENU_ITEM_ID, tbMenuOwner, R.id.menu_item),
               RecyclerItemState(RECYCLER_ITEM_ID, rvOwner, RECYCLER_ITEM_INDEX),
               PathState(PATH_ID, path)
}

private var tutorial: BlurTutorial? = null

private val tutorialListener = object : SimpleTutorialListener() {

        override fun onPopupViewInflated(state: TutorialState, popupView: View) {
            popupView.run {
                val tvTitle = findViewById<TextView>(R.id.tvTitle)
                
                tvTitle.textResource = when (state.id) {
                    VIEW_ID -> R.string.view_title
                    MENU_ITEM_ID -> R.string.menu_title
                    RECYCLER_ITEM_ID -> R.string.recycler_title
                    PATH_ID -> R.string.path_state
                }
                setOnClickListener { tutorial?.next() }
            }
        }
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTutorial()
    }
    
    override fun onResume() {
        super.onResume()
        tutorial?.start()
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tutorial?.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        tutorial?.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        tutorial?.onDestroy()
        super.onDestroy()
    }
    
    private fun initTutorial() {
        tutorial = TutorialBuilder()
                .withParent(clParent)
                .withListener(tutorialListener)
                .withPopupLayout(R.layout.popup_window)
                .withPopupCornerRadius(POPUP_RADIUS)
                .withBlurRadius(BLUR_RADIUS)
                .build().apply { addAllStates(states) }
    }
```

#### Proguard ####
Also depending on your Proguard config, you have to setup your proguard-rules.pro file:

```proguard
-keepclassmembers class androidx.appcompat.widget.Toolbar { *; }

-keep class com.google.android.material.bottomnavigation.BottomNavigationView
-keepclassmembers class com.google.android.material.bottomnavigation.BottomNavigationView { *; }

-keep class com.google.android.material.bottomnavigation.BottomNavigationItemView
-keepclassmembers class com.google.android.material.bottomnavigation.BottomNavigationItemView { *; }

-keep class androidx.appcompat.view.menu.MenuItemImpl
-keepclassmembers class androidx.appcompat.view.menu.MenuItemImpl { *; }

-keep class com.google.android.material.navigation.NavigationView
-keepclassmembers class com.google.android.material.navigation.NavigationView { *; }

-keep class com.google.android.material.internal.NavigationMenuPresenter** { *; }
-keepclassmembers class com.google.android.material.internal.NavigationMenuPresenter { *; }

-keep class com.google.android.material.bottomappbar.BottomAppBar
-keepclassmembers class com.google.android.material.bottomappbar.BottomAppBar { *; }
```

### License ###
* * *
    The MIT License (MIT)
    
    Copyright (c) 2016 Cleveroad Inc.
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

