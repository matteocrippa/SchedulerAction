# SchedulerAction
An action/function/lambda scheduler with start, stop, pause function

## Setup

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```
dependencies {
	        compile 'com.github.matteocrippa:SchedulerAction:0.1.0'
	}
```

## Usage

First setup your scheduler
```kotlin
    val timer = SchedulerAction("name.you.prefer")
```

Schedule an action
```kotlin
timer += ActionTask(1000, 0, {
            runOnUiThread {
                // your action
            }
        })
```
        
`action()` func needs 3 parameters:
- at 
- action
- duration (optional)


Then you will be able to use:

- play()
- pause()
- stop()
- prev()
- next()

functions to manage the scheduler.

`stop()` pause the scheduler first and the rewind it to the beginning.