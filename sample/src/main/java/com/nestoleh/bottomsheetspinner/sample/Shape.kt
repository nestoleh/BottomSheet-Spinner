package com.nestoleh.bottomsheetspinner.sample

import androidx.annotation.DrawableRes

enum class Shape(
    val title: String,
    val description: String,
    @DrawableRes
    val drawableRes: Int
) {
    Circle(
        title = "Circle",
        description = "A circle is a shape consisting of all points in a plane that are a given distance from a given poin",
        drawableRes = R.drawable.ic_circle
    ),

    Triangle(
        title = "Triangle",
        description = "A triangle is a polygon with three edges and three vertices",
        drawableRes = R.drawable.ic_triangle
    ),

    Square(
        title = "Square",
        description = "A square is a regular quadrilateral, which means that it has four equal sides and four equal angles (90-degree angles, or 100-gradian angles or right angles)",
        drawableRes = R.drawable.ic_square
    ),

    Pentagon(
        title = "Pentagon",
        description = "In geometry, a pentagon is any five-sided polygon or 5-gon",
        drawableRes = R.drawable.ic_pentagon
    ),

    Hexagon(
        title = "Hexagone",
        description = "A hexagon is a six-sided polygon or 6-gon",
        drawableRes = R.drawable.ic_hexagon
    )
}