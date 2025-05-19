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

# Example Folder
$examples = "$PSScriptRoot\examples"

$Error.clear()
Get-Content "$examples\bad_printing.timeline" | java -ea "$interp" |
    Assert-Equals ("AMORPHOUSAMORPHOUSAMORPHOUSUNEVALUATEDAAAA") &&
Get-Content "$examples\cat.timeline" | java -ea "$interp" "This is a wonderful cat program!" |
    Assert-Equals ("This is a wonderful cat program!") &&
Get-Content "$examples\circle_effect.timeline" | java -ea "$interp" |
    Assert-Equals ("0123") &&
Get-Content "$examples\dropoff_rigorous.timeline" | java -ea "$interp" |
    Assert-Equals ("AABACADAEAFAGAHAIAJA") &&
Get-Content "$examples\dropoff.timeline" | java -ea "$interp" |
    Assert-Equals ("10") &&
# Get-Content "$examples\fibonacci_copy.timeline" | java -ea "$interp" &&
Get-Content "$examples\hello.timeline" | java -ea "$interp" |
    Assert-Equals ("Hello, World!") &&
Get-Content "$examples\hi.timeline" | java -ea "$interp" |
    Assert-Equals ("Hi") &&
Get-Content "$examples\horizontal_looping.timeline" | java -ea "$interp" |
    Assert-Equals ("00") &&
Get-Content "$examples\infinity_cell_loop.timeline" | java -ea "$interp" |
    Assert-Equals ("012345678901234567890123456789") &&
Get-Content "$examples\math_operators.timeline" | java -ea "$interp" |
    Assert-Equals ("4") &&
Get-Content "$examples\vertical_looping.timeline" | java -ea "$interp" |
    Assert-Equals ("a") &&
ForEach-Object 'foo';

if ($Error -or !$?) { 
    "*** TIMELINE TESTS FAILED ***" 
}
else { 
    "TIMELINE TESTS PASSED" 
} 