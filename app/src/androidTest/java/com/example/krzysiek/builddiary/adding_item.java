package com.example.krzysiek.builddiary;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.ContentValues.TAG;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class adding_item {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void add_item() {

        for (int i = 0; i <= 300; i++) {
            Log.i(TAG, String.valueOf(i));

            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.addbutton), withText("Dodaj"),
                            childAtPosition(
                                    allOf(withId(R.id.RelativeLayout2),
                                            childAtPosition(
                                                    withId(R.id.LinearLayout1), 1)),
                                    5),
                            isDisplayed()));
            appCompatButton.perform(click());

            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.title),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.ScrollView")),
                                            0),
                                    1)));
            appCompatEditText.perform(scrollTo(), replaceText("pozycja "+ 100+i), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.title), withText("pozycja "+ 100+i),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.ScrollView")),
                                            0),
                                    1)));
            appCompatEditText2.perform(scrollTo(), click());

            ViewInteraction appCompatEditText5 = onView(
                    allOf(withId(R.id.cost),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.ScrollView")),
                                            0),
                                    3)));
            appCompatEditText5.perform(scrollTo(), replaceText(String.valueOf(i*10 + i*7)), closeSoftKeyboard());

            ViewInteraction appCompatEditText6 = onView(
                    allOf(withId(R.id.cost), withText(String.valueOf(i*10 + i*7)),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.ScrollView")),
                                            0),
                                    3)));
            appCompatEditText6.perform(pressImeActionButton());

            ViewInteraction appCompatButton2 = onView(
                    allOf(withId(R.id.submitButton), withText("Dodaj"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.RelativeLayout")),
                                            9),
                                    2)));
            appCompatButton2.perform(scrollTo(), click());

        }
    }
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
