use std::io::{self, BufRead};
use std::collections::HashMap;

fn main() {

    let mm1 = HashMap::from([
        ("B X", 1),
        ("C Y", 2),
        ("A Z", 3),
        ("A X", 4),
        ("B Y", 5),
        ("C Z", 6),
        ("C X", 7),
        ("A Y", 8),
        ("B Z", 9),
    ]);

    let mm2 = HashMap::from([
        ("B X", 1),
        ("C X", 2),
        ("A X", 3),
        ("A Y", 4),
        ("B Y", 5),
        ("C Y", 6),
        ("C Z", 7),
        ("A Z", 8),
        ("B Z", 9),
    ]);

    let mut sum1:u32 = 0;
    let mut sum2:u32 = 0;

    for iter in io::stdin().lock().lines() {
        let line = iter.unwrap();
        match mm1.get(&line as &str) {
            Some(v) => sum1 += v,
            None => ()
        }
        match mm2.get(&line as &str) {
            Some(v) => sum2 += v,
            None => ()
        }
    }
    println!("{}", sum1);
    println!("{}", sum2);
}
