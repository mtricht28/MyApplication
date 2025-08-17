package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvScore: TextView
    private var attemptCount = 0

    private val cardIds = mutableListOf(0, 0, 1, 1, 2, 2, 3, 3, 4)
    private lateinit var buttons: List<ImageButton>
    private var firstIndex: Int? = null
    private var matchedPairs = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // スコア表示のバインド＆初期更新
        tvScore = findViewById(R.id.tvScore)
        updateScore()

        // カードをランダムに配置
        cardIds.shuffle()

        // ImageButton群の取得とクリック設定
        buttons = (0 until 9).map { i ->
            findViewById<ImageButton>(
                resources.getIdentifier("card$i", "id", packageName)
            ).apply {
                setImageResource(R.drawable.card_back)
                setOnClickListener { onCardClicked(i) }
            }
        }
    }

    private fun onCardClicked(index: Int) {
        val btn = buttons[index]
        if (!btn.isEnabled || firstIndex == index) return

        // タップ回数カウント＆表示更新
        attemptCount++
        updateScore()

        // 表向きに
        btn.setImageResource(getFaceResource(cardIds[index]))

        // ジョーカー判定
        if (cardIds[index] == 4) {
            showGameOver()
            return
        }

        if (firstIndex == null) {
            firstIndex = index
        } else {
            val prev = firstIndex!!
            if (cardIds[prev] == cardIds[index]) {
                // マッチ成功
                buttons[prev].isEnabled = false
                btn.isEnabled = false
                matchedPairs++
                updateScore()
                if (matchedPairs == 4) showGameClear()
            } else {
                // ミスマッチ → 800ms後に裏返し
                handler.postDelayed({
                    buttons[prev].setImageResource(R.drawable.card_back)
                    btn.setImageResource(R.drawable.card_back)
                }, 800)
            }
            firstIndex = null
        }
    }

    private fun getFaceResource(id: Int): Int = when (id) {
        0 -> R.drawable.card_0
        1 -> R.drawable.card_1
        2 -> R.drawable.card_2
        3 -> R.drawable.card_3
        4 -> R.drawable.joker
        else -> R.drawable.card_back
    }

    private fun updateScore() {
        tvScore.text = "Matched: $matchedPairs / 4   Attempts: $attemptCount"
    }

    private fun showGameOver() {
        val view = layoutInflater.inflate(R.layout.dialog_retry, null)
        view.findViewById<TextView>(R.id.tvDialogTitle).text = "Game Over"
        view.findViewById<TextView>(R.id.tvDialogMessage).text = "ジョーカーをめくってしまいました…"
        view.findViewById<Button>(R.id.btnRetry).text = "再挑戦"

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        view.findViewById<Button>(R.id.btnRetry).setOnClickListener {
            dialog.dismiss()
            recreate()
        }

        dialog.show()
    }

    private fun showGameClear() {
        val view = layoutInflater.inflate(R.layout.dialog_retry, null)
        view.findViewById<TextView>(R.id.tvDialogTitle).text = "Congratulations!"
        view.findViewById<TextView>(R.id.tvDialogMessage).text = "4 ペア揃いました！クリア！"
        view.findViewById<Button>(R.id.btnRetry).text = "もう一度"

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        view.findViewById<Button>(R.id.btnRetry).setOnClickListener {
            dialog.dismiss()
            recreate()
        }

        dialog.show()
    }
}
