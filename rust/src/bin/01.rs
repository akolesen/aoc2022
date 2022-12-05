use std::io::{self, BufRead};

fn main() {
    let mut v = vec![0;1];
    for iter in io::stdin().lock().lines() {
        let line = iter.unwrap();
        if line.is_empty() {
            v.push(0);
        }
        else {
            let last = v.last_mut().unwrap();
            *last += line.parse::<i32>().unwrap();
        }
    }

    v.sort();
    match v.iter().rev().next() {
        Some(x) => println!("{}", x),
        None    => (),
    }

    let top3sum:u32 = v.iter().rev().take(3).map(|&i| i as u32).sum();
    println!("{}", top3sum);
}
