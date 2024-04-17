package com.example.expenseease
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF

class Graph : AppCompatActivity() {
    private lateinit var pieChart: PieChart
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        db = AppDatabase.getDatabase(this)

        pieChart = findViewById(R.id.chart)
        setupChart()
        loadChartData()
    }

    private fun setupChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(android.R.color.white)
        pieChart.setTransparentCircleColor(android.R.color.white)
        pieChart.setTransparentCircleAlpha(110)
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.setDrawCenterText(true)
        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        val l: Legend = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f
    }

    private fun loadChartData() {
        db.expenseDao().getAllExpenses().observe(this, Observer { expenses ->
            if (expenses != null) {
                val expensesByCategory = expenses.groupBy { it.category }
                    .mapValues { (_, expenseItems) -> expenseItems.sumOf { it.amount } }

                val entries = expensesByCategory.map { (category, amount) ->
                    PieEntry(amount.toFloat(), category)
                }
                val dataSet = PieDataSet(entries, "Expense Categories")
                dataSet.setDrawIcons(false)
                dataSet.sliceSpace = 3f
                dataSet.iconsOffset = MPPointF(0f, 40f) // Using MPPointF here
                dataSet.selectionShift = 5f
                dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

                val data = PieData(dataSet)
                data.setValueTextSize(11f)
                data.setValueTextColor(android.graphics.Color.WHITE)
                pieChart.data = data
                pieChart.invalidate() // refresh
            }
        })
    }
}
