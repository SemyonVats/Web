<#import "./common.ftlh" as c>

<@c.page>
    <h1>Tic-Tac-Toe</h1>

    <form method="post">
        <input type="hidden" name="action" value="onMove">

        <#list 0..state.size-1 as row>
            <div class="tictactoe-row">
                <#list 0..state.size-1 as col>
                    <#if state.cells[row][col]??>
                        <button type="button" class="tictactoe-cell taken" disabled>
                            ${state.cells[row][col]}
                        </button>
                    <#else>
                        <button type="submit" name="cell_${row}_${col}" value="1" class="tictactoe-cell">
                            &nbsp;
                        </button>
                    </#if>
                </#list>
            </div>
        </#list>
    </form>

    <div class="tictactoe-message">
        <#if state.phase == "RUNNING">
            Move: ${state.currentPlayer}
        <#elseif state.phase == "WON_X">
            Game over. Xs won!
        <#elseif state.phase == "WON_O">
            Game over. Os won!
        <#elseif state.phase == "DRAW">
            Game over. Draw!
        </#if>
    </div>

    <#if state.phase != "RUNNING">
        <form method="post">
            <input type="hidden" name="action" value="newGame">
            <input type="submit" class="tictactoe-new-game" value="New Game!">
        </form>
    </#if>
</@c.page>