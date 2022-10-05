package me.theseems.tinybench.command

class TinyBenchCommand : TinyBenchCommandContainer() {
    init {
        add(ReloadSubCommand())
    }
}
