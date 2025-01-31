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
        [Parameter(Mandatory = $true)]$Expected,
        [Parameter(Mandatory = $true, ValueFromPipeline)]$Output
    )
    
    if ($Output -ne $Expected) {
        Write-Error "Output does not match expected results.`nOUTPUT: $Output`nEXPECTED: $Expected"
    }
}

# Timeline Interpreter Location
$interp = "$PSScriptRoot\src\TimelineInterpreter.java"

$Error.clear()
Get-Content "$PSScriptRoot\examples\bad_printing.timeline" | java -ea "$interp" |
    Assert-Equals ("AMORPHOUSAMORPHOUSAMORPHOUSUNEVALUATEDAAAA") &&
Get-Content "$PSScriptRoot\examples\circle_effect.timeline" | java -ea "$interp" |
    Assert-Equals ("0123") &&
Get-Content "$PSScriptRoot\examples\dropoff_rigorous.timeline" | java -ea "$interp" |
    Assert-Equals ("AABACADAEAFAGAHAIAJA") &&
Get-Content "$PSScriptRoot\examples\dropoff.timeline" | java -ea "$interp" |
    Assert-Equals ("10") &&
# Get-Content "$PSScriptRoot\examples\hello_world.timeline" | java -ea "$interp" |
#     Assert-Equals "Hello, World!" &&
Get-Content "$PSScriptRoot\examples\hi.timeline" | java -ea "$interp" |
    Assert-Equals ("Hi") &&
Get-Content "$PSScriptRoot\examples\horizontal_looping.timeline" | java -ea "$interp" |
    Assert-Equals ("00") &&
Get-Content "$PSScriptRoot\examples\infinity_cell_loop.timeline" | java -ea "$interp" |
    Assert-Equals ("012345678901234567890123456789") &&
Get-Content "$PSScriptRoot\examples\math_operators.timeline" | java -ea "$interp" |
    Assert-Equals ("4") &&
Get-Content "$PSScriptRoot\examples\vertical_looping.timeline" | java -ea "$interp" |
    Assert-Equals ("a") &&
ForEach-Object 'foo';

if ($Error -or !$?) { 
    "*** TIMELINE TESTS FAILED ***" 
}
else { 
    "TIMELINE TESTS PASSED" 
} 