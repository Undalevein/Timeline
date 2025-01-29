function Assert-MatchTests {
    param (
        [Parameter(Mandatory = $true, ValueFromPipeline)] $TestResult
    )
    
    if ($TestResult) {
        Write-Error "Output does not match expected results."
    }
}

function Assert-Equals {
    param (
        [Parameter(Mandatory = $true, ValueFromPipeline)] $Output,
        [Parameter(Mandatory = $true)] $Expected
    )
    
    if ($Output -ne $Expected) {
        Write-Error "Output does not match expected results.`nOUTPUT: $Output`nEXPECTED: $Expected"
    }
}

# Timeline Interpreter Location
$interp = "$PSScriptRoot\TimelineInterpreter.java"

$Error.clear()
Get-Content "$PSScriptRoot\bad_printing.timeline" | java -ea "$interp" |
    Assert-Equals ("AMORPHOUSAMORPHOUSAMORPHOUSUNEVALUATEDAAAA") &&
Get-Content "$PSScriptRoot\circle_effect.timeline" | java -ea "$interp" |
    Assert-Equals ("0123") &&
# Get-Content "$PSScriptRoot\hello_world.timeline" | java -ea "$interp" |
#     Assert-Equals "Hello, World!" &&
Get-Content "$PSScriptRoot\hi.timeline" | java -ea "$interp" |
    Assert-Equals ("Hi") &&
Get-Content "$PSScriptRoot\horizontal_looping.timeline" | java -ea "$interp" |
    Assert-Equals ("00") &&
Get-Content "$PSScriptRoot\infinity_cell_loop.timeline" | java -ea "$interp" |
    Assert-Equals ("012345678901234567890123456789") &&
Get-Content "$PSScriptRoot\math_operators.timeline" | java -ea "$interp" |
    Assert-Equals ("4") &&
Get-Content "$PSScriptRoot\vertical_looping.timeline" | java -ea "$interp" |
    Assert-Equals ("a") &&
ForEach-Object 'foo';

if ($Error -or !$?) { 
    "*** TIMELINE TESTS FAILED ***" 
}
else { 
    "TIMELINE TESTS PASSED" 
} 