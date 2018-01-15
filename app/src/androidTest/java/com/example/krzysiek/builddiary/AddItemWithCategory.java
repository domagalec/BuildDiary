package com.example.krzysiek.builddiary;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddItemWithCategory {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addItemWithCategory() {
        int j=0;

        for (int i = 0; i <= 300; i++) {


            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.addbutton), withText("Dodaj"),
                            childAtPosition(
                                    allOf(withId(R.id.RelativeLayout2),
                                            childAtPosition(
                                                    withId(R.id.LinearLayout1),
                                                    1)),
                                    5),
                            isDisplayed()));
            appCompatButton.perform(click());

            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.title),
                            childAtPosition(
                                    allOf(withId(R.id.RelativeLayout1),
                                            childAtPosition(
                                                    withId(R.id.LinearLayout1),
                                                    0)),
                                    1)));
            appCompatEditText.perform(scrollTo(), replaceText("Nazwa pozycji" + i), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.cost),
                            childAtPosition(
                                    allOf(withId(R.id.RelativeLayout1),
                                            childAtPosition(
                                                    withId(R.id.LinearLayout1),
                                                    0)),
                                    3)));
            appCompatEditText2.perform(scrollTo(), replaceText(String.valueOf(10 * i + 0.13 * i)), closeSoftKeyboard());

            ViewInteraction appCompatSpinner = onView(
                    allOf(withId(R.id.category_spinner),
                            childAtPosition(
                                    allOf(withId(R.id.RelativeLayout1),
                                            childAtPosition(
                                                    withId(R.id.LinearLayout1),
                                                    0)),
                                    5)));
            appCompatSpinner.perform(scrollTo(), click());


            DataInteraction appCompatCheckedTextView = onData(anything())
                    .inAdapterView(childAtPosition(
                            withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                            0))
                    .atPosition(j);
            appCompatCheckedTextView.perform(click());

            j++;

            if (j>12){
                j=0;
            }


                ViewInteraction appCompatButton2 = onView(
                    allOf(withId(R.id.submitButton), withText("Dodaj"),
                            childAtPosition(
                                    allOf(withId(R.id.RelativeLayout2),
                                            childAtPosition(
                                                    withId(R.id.LinearLayout1),
                                                    1)),
                                    1)));
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
